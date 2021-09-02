package com.Deeakron.journey_mode.client.gui;

import com.Deeakron.journey_mode.JourneyModePowersContainer;
import com.Deeakron.journey_mode.client.event.MenuSwitchEvent;
import com.Deeakron.journey_mode.journey_mode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.settings.HotbarSnapshot;
import net.minecraft.client.util.ISearchTree;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public class JourneyModeDuplicationScreen extends ContainerScreen<JourneyModeDuplicationScreen.DuplicationContainer> /*DisplayEffectsScreen<JourneyModeDuplicationScreen.DuplicationContainer>*/ {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(journey_mode.MODID, "textures/gui/jm_duplication.png");
    public static final ITextComponent POWERS_TAB = new TranslationTextComponent("journey_mode.gui.tabs.powers");
    public static final ITextComponent RESEARCH_TAB = new TranslationTextComponent("journey_mode.gui.tabs.research");
    public static final ITextComponent DUPLICATION_TAB = new TranslationTextComponent("journey_mode.gui.tabs.duplication");
    private static final Inventory TMP_INVENTORY = new Inventory(45);
    private static final ITextComponent field_243345_D = new TranslationTextComponent("inventory.binSlot");

    private static int selectedTabIndex; // need to put something

    private float currentScroll;

    private boolean isScrolling;
    private TextFieldWidget searchField;
    @Nullable
    private List<Slot> originalSlots;
    @Nullable
    private Slot destroyItemSlot;
    private DuplicationListener listener;
    private boolean field_195377_F;
    private static int tabPage = 0;
    private int maxPages = 0;
    private boolean field_199506_G;
    private final Map<ResourceLocation, ITag<Item>> tagSearchResults = Maps.newTreeMap();

    public JourneyModeDuplicationScreen(PlayerEntity player) {
        super (new JourneyModeDuplicationScreen.DuplicationContainer(player), player.inventory, StringTextComponent.EMPTY);
        player.openContainer = this.container;
        this.passEvents = true;
        this.xSize = 190;
        this.ySize = 183;
        for (int i = 0; i < ItemGroup.GROUPS.length; i++) {
            journey_mode.LOGGER.info("current group " + i + " is: " + ItemGroup.GROUPS[i].getGroupName().getString());
        }
    }

    protected void handleMouseClick(@Nullable Slot slotIn, int slotId, int mouseButton, ClickType type) {
        if (this.hasTmpInventory(slotIn)) {
            this.searchField.setCursorPositionEnd();
            this.searchField.setSelectionPos(0);
        }

        boolean flag = type == ClickType.QUICK_MOVE;
        type = slotId == -999 && type == ClickType.PICKUP ? ClickType.THROW : type;
        if (slotIn == null && selectedTabIndex != ItemGroup.INVENTORY.getIndex() && type != ClickType.QUICK_CRAFT) {
            PlayerInventory playerInventory1 = this.minecraft.player.inventory;
            if (!playerInventory1.getItemStack().isEmpty() && this.field_199506_G) {
                if (mouseButton == 0) {
                    this.minecraft.player.dropItem(playerInventory1.getItemStack(), true);
                    this.minecraft.playerController.sendPacketDropItem(playerInventory1.getItemStack());
                    playerInventory1.setItemStack(ItemStack.EMPTY);
                }

                if (mouseButton == 1) {
                    ItemStack itemStack6 = playerInventory1.getItemStack().split(1);
                    this.minecraft.player.dropItem(itemStack6, true);
                    this.minecraft.playerController.sendPacketDropItem(itemStack6);
                }
            }
        } else {
            if (slotIn != null && !slotIn.canTakeStack(this.minecraft.player)) {
                return;
            }

            if (slotIn == this.destroyItemSlot && flag) {
                for (int j = 0; j < this.minecraft.player.container.getInventory().size(); j++) {
                    this.minecraft.playerController.sendSlotPacket(ItemStack.EMPTY, j);
                }
            } else if (selectedTabIndex == ItemGroup.INVENTORY.getIndex()) {
                if (slotIn == this.destroyItemSlot) {
                    this.minecraft.player.inventory.setItemStack(ItemStack.EMPTY);
                } else if (type == ClickType.THROW && slotIn != null && slotIn.getHasStack()) {
                    ItemStack itemStack = slotIn.decrStackSize(mouseButton == 0 ? 1 : slotIn.getStack().getMaxStackSize());
                    ItemStack itemStack1 = slotIn.getStack();
                    this.minecraft.player.dropItem(itemStack, true);
                    this.minecraft.playerController.sendPacketDropItem(itemStack);
                    this.minecraft.playerController.sendSlotPacket(itemStack1, ((JourneyModeDuplicationScreen.DuplicationSlot)slotIn).slot.slotNumber);
                } else if (type == ClickType.THROW && !this.minecraft.player.inventory.getItemStack().isEmpty()) {
                    this.minecraft.player.dropItem(this.minecraft.player.inventory.getItemStack(), true);
                    this.minecraft.playerController.sendPacketDropItem(this.minecraft.player.inventory.getItemStack());
                    this.minecraft.player.inventory.setItemStack(ItemStack.EMPTY);
                } else {
                    this.minecraft.player.container.slotClick(slotIn == null ? slotId : ((JourneyModeDuplicationScreen.DuplicationSlot)slotIn).slot.slotNumber, mouseButton, type, this.minecraft.player);
                    this.minecraft.player.container.detectAndSendChanges();
                }
            } else if (type != ClickType.QUICK_CRAFT && slotIn.inventory == TMP_INVENTORY) {
                PlayerInventory playerInventory = this.minecraft.player.inventory;
                ItemStack itemStack5 = playerInventory.getItemStack();
                ItemStack itemStack7 = slotIn.getStack();
                if (type == ClickType.SWAP) {
                    if (!itemStack7.isEmpty()) {
                        ItemStack itemStack10 = itemStack7.copy();
                        itemStack10.setCount(itemStack10.getMaxStackSize());
                        this.minecraft.player.inventory.setInventorySlotContents(mouseButton, itemStack10);
                        this.minecraft.player.container.detectAndSendChanges();
                    }

                    return;
                }

                if (type == ClickType.CLONE) {
                    if (playerInventory.getItemStack().isEmpty() && slotIn.getHasStack()) {
                        ItemStack itemStack9 = slotIn.getStack().copy();
                        itemStack9.setCount(itemStack9.getMaxStackSize());
                        playerInventory.setItemStack(itemStack9);
                    }

                    return;
                }

                if (type == ClickType.THROW) {
                    if (!itemStack7.isEmpty()) {
                        ItemStack itemStack8 = itemStack7.copy();
                        itemStack8.setCount(mouseButton == 0 ? 1 : itemStack8.getMaxStackSize());
                        this.minecraft.player.dropItem(itemStack8, true);
                        this.minecraft.playerController.sendPacketDropItem(itemStack8);
                    }

                    return;
                }

                if (!itemStack5.isEmpty() && !itemStack7.isEmpty() && itemStack5.isItemEqual(itemStack7) && ItemStack.areItemStackTagsEqual(itemStack5, itemStack7)) {
                    if (mouseButton == 0) {
                        if (flag) {
                            itemStack5.setCount(itemStack5.getMaxStackSize());
                        } else if (itemStack5.getCount() < itemStack5.getMaxStackSize()) {
                            itemStack5.grow(1);
                        }
                    } else {
                        itemStack5.shrink(1);
                    }
                } else if (!itemStack7.isEmpty() && itemStack5.isEmpty()) {
                    playerInventory.setItemStack(itemStack7.copy());
                    itemStack5 = playerInventory.getItemStack();
                    if (flag) {
                        itemStack5.setCount(itemStack5.getMaxStackSize());
                    }
                } else if (mouseButton == 0) {
                    playerInventory.setItemStack(ItemStack.EMPTY);
                } else {
                    playerInventory.getItemStack().shrink(1);
                }
            } else if (this.container != null) {
                ItemStack itemStack3 = slotIn == null ? ItemStack.EMPTY : this.container.getSlot(slotIn.slotNumber).getStack();
                this.container.slotClick(slotIn == null ? slotId : slotIn.slotNumber, mouseButton, type, this.minecraft.player);
                if (Container.getDragEvent(mouseButton) == 2) {
                    for (int k = 0; k < 9; k++) {
                        this.minecraft.playerController.sendSlotPacket(this.container.getSlot(36 + k).getStack(), 27 + k);
                    }
                } else if (slotIn != null) {
                    ItemStack itemStack4 = this.container.getSlot(slotIn.slotNumber).getStack();
                    this.minecraft.playerController.sendSlotPacket(itemStack4, slotIn.slotNumber - (this.container).inventorySlots.size() + 9 + 27);
                } else if (type == ClickType.THROW && !itemStack3.isEmpty()) {
                    ItemStack itemStack2 = itemStack3.copy();
                    itemStack2.setCount(mouseButton == 0 ? 1 : itemStack2.getMaxStackSize());
                    this.minecraft.player.dropItem(itemStack2, true);
                    this.minecraft.playerController.sendPacketDropItem(itemStack2);
                }

                this.minecraft.player.container.detectAndSendChanges();
            }
        }
    }

    private boolean hasTmpInventory(@Nullable Slot slotIn) {
        return slotIn != null && slotIn.inventory == TMP_INVENTORY;
    }

    protected void init() {
        super.init();
        this.addButton(new JourneyModeDuplicationScreen.PowersTab(this.guiLeft -29, this.guiTop + 21));
        this.addButton(new JourneyModeDuplicationScreen.ResearchTab(this.guiLeft -29, this.guiTop + 50));
        this.addButton(new JourneyModeDuplicationScreen.DuplicationTab(this.guiLeft -29, this.guiTop + 79));
        int tabCount = ItemGroup.GROUPS.length;
        if (tabCount > 6) {
            //add new tab buttons
            maxPages = (int) Math.ceil((tabCount - 6) / 10D);
        }
        this.minecraft.keyboardListener.enableRepeatEvents(true);
        this.searchField = new TextFieldWidget(this.font, this.guiLeft + 81, this.guiTop + 9, 80, 9, new TranslationTextComponent("itemGroup.search"));
        this.searchField.setMaxStringLength(50);
        this.searchField.setEnableBackgroundDrawing(false);
        this.searchField.setVisible(false);
        this.searchField.setTextColor(16777215);
        this.children.add(this.searchField);
        int i = selectedTabIndex;
        selectedTabIndex = -1;
        this.setCurrentDuplicationTab(ItemGroup.GROUPS[i]);
        this.minecraft.player.container.removeListener(this.listener);
        this.listener = new DuplicationListener(this.minecraft);
        this.minecraft.player.container.addListener(this.listener);
    }

    public void resize(Minecraft minecraft, int width, int height) {
        String s = this.searchField.getText();
        this.init(minecraft, width, height);
        this.searchField.setText(s);
        if (!this.searchField.getText().isEmpty()) {
            this.updateDuplicationSearch();
        }
    }

    public void onClose() {
        super.onClose();
        if (this.minecraft.player != null && this.minecraft.player.inventory != null) {
            this.minecraft.player.container.removeListener(this.listener);
        }

        this.minecraft.keyboardListener.enableRepeatEvents(false);
    }

    public boolean charTyped(char codePoint, int modifiers) {
        if (this.field_195377_F) {
            return false;
        } else if (!ItemGroup.GROUPS[selectedTabIndex].hasSearchBar()) {
            return false;
        } else {
            String s = this.searchField.getText();
            if (this.searchField.charTyped(codePoint, modifiers)) {
                if (!Objects.equals(s, this.searchField.getText())) {
                    this.updateDuplicationSearch();
                }

                return true;
            } else {
                return false;
            }
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.field_195377_F = false;
        if (!ItemGroup.GROUPS[selectedTabIndex].hasSearchBar()) {
            if (this.minecraft.gameSettings.keyBindChat.matchesKey(keyCode, scanCode)) {
                this.field_195377_F = true;
                this.setCurrentDuplicationTab(ItemGroup.SEARCH);
                return true;
            } else {
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
        } else {
            boolean flag = !this.hasTmpInventory(this.hoveredSlot) || this.hoveredSlot.getHasStack();
            boolean flag1 = InputMappings.getInputByCode(keyCode, scanCode).func_241552_e_().isPresent();
            if (flag && flag1 && this.itemStackMoved(keyCode, scanCode)) {
                this.field_195377_F = true;
                return true;
            } else {
                String s = this.searchField.getText();
                if (this.searchField.keyPressed(keyCode, scanCode, modifiers)) {
                    if (!Objects.equals(s, this.searchField.getText())) {
                        this.updateDuplicationSearch();
                    }

                    return true;
                } else {
                    return this.searchField.isFocused() && this.searchField.getVisible() && keyCode != 256 ? true : super.keyPressed(keyCode, scanCode, modifiers);
                }
            }
        }
    }

    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        this.field_195377_F = false;
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void updateDuplicationSearch() {
        (this.container).itemList.clear();
        this.tagSearchResults.clear();

        ItemGroup tab = ItemGroup.GROUPS[selectedTabIndex];
        if (tab.hasSearchBar() && tab != ItemGroup.SEARCH) {
            tab.fill(container.itemList);
            if (!this.searchField.getText().isEmpty()) {
                String search = this.searchField.getText().toLowerCase(Locale.ROOT);
                java.util.Iterator<ItemStack> itr = container.itemList.iterator();
                while (itr.hasNext()) {
                    ItemStack stack = itr.next();
                    boolean matches = false;
                    for (ITextComponent line : stack.getTooltip(this.minecraft.player, this.minecraft.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL)) {
                        if (TextFormatting.getTextWithoutFormattingCodes(line.getString()).toLowerCase(Locale.ROOT).contains(search)) {
                            matches = true;
                            break;
                        }
                    }
                    if (!matches)
                        itr.remove();
                }
            }
            this.currentScroll = 0.0F;
            container.scrollTo(0.0F);
            return;
        }

        String s = this.searchField.getText();
        if (s.isEmpty()) {
            for(Item item : Registry.ITEM) {
                item.fillItemGroup(ItemGroup.SEARCH, (this.container).itemList);
            }
        } else {
            ISearchTree<ItemStack> isearchtree;
            if (s.startsWith("#")) {
                s = s.substring(1);
                isearchtree = this.minecraft.getSearchTree(SearchTreeManager.TAGS);
                this.searchTags(s);
            } else {
                isearchtree = this.minecraft.getSearchTree(SearchTreeManager.ITEMS);
            }

            (this.container).itemList.addAll(isearchtree.search(s.toLowerCase(Locale.ROOT)));
        }

        this.currentScroll = 0.0F;
        this.container.scrollTo(0.0F);
    }

    private void searchTags(String search) {
        int i = search.indexOf(58);
        Predicate<ResourceLocation> predicate;
        if (i == -1) {
            predicate = (p_214084_1_) -> {
                return p_214084_1_.getPath().contains(search);
            };
        } else {
            String s = search.substring(0, i).trim();
            String s1 = search.substring(i + 1).trim();
            predicate = (p_214081_2_) -> {
                return p_214081_2_.getNamespace().contains(s) && p_214081_2_.getPath().contains(s1);
            };
        }

        ITagCollection<Item> itagcollection = ItemTags.getCollection();
        itagcollection.getRegisteredTags().stream().filter(predicate).forEach((p_214082_2_) -> {
            ITag itag = this.tagSearchResults.put(p_214082_2_, itagcollection.get(p_214082_2_));
        });
    }

    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        ItemGroup itemgroup = ItemGroup.GROUPS[selectedTabIndex];
        if (itemgroup != null && itemgroup.drawInForegroundOfGroup()) {
            RenderSystem.disableBlend();
            this.font.drawText(matrixStack, itemgroup.getGroupName(), 8.0F, 6.0F, itemgroup.getLabelColor());
        }

        for(Widget widget : this.buttons) {
            if (widget.isHovered()) {
                widget.renderToolTip(matrixStack, x - this.guiLeft, y - this.guiTop);
                break;
            }
        }

    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            double d0 = mouseX - (double)this.guiLeft;
            double d1 = mouseY - (double)this.guiTop;

            for(ItemGroup itemgroup : ItemGroup.GROUPS) {
                if (itemgroup != null && this.isMouseOverGroup(itemgroup, d0, d1)) {
                    return true;
                }
            }

            if (selectedTabIndex != ItemGroup.INVENTORY.getIndex() && this.func_195376_a(mouseX, mouseY)) {
                this.isScrolling = this.needsScrollBars();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            double d0 = mouseX - (double)this.guiLeft;
            double d1 = mouseY - (double)this.guiTop;
            this.isScrolling = false;

            for(ItemGroup itemgroup : ItemGroup.GROUPS) {
                if (itemgroup != null && this.isMouseOverGroup(itemgroup, d0, d1)) {
                    this.setCurrentDuplicationTab(itemgroup);
                    return true;
                }
            }
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean needsScrollBars() {
        if (ItemGroup.GROUPS[selectedTabIndex] == null) return false;
        return selectedTabIndex != ItemGroup.INVENTORY.getIndex() && ItemGroup.GROUPS[selectedTabIndex].hasScrollbar() && this.container.canScroll();
    }

    private void setCurrentDuplicationTab(ItemGroup tab) {
        if (tab == null) return;
        int i = selectedTabIndex;
        selectedTabIndex = tab.getIndex();
        slotColor = tab.getSlotColor();
        this.dragSplittingSlots.clear();
        (this.container).itemList.clear();
        if (tab != ItemGroup.SEARCH) {
            tab.fill((this.container).itemList);
        }

        if (tab == ItemGroup.INVENTORY) {
            Container container = this.minecraft.player.container;
            if (this.originalSlots == null) {
                this.originalSlots = ImmutableList.copyOf((this.container).inventorySlots);
            }

            (this.container).inventorySlots.clear();

            for(int l = 0; l < container.inventorySlots.size(); ++l) {
                int i1;
                int j1;
                if (l >= 5 && l < 9) {
                    int l1 = l - 5;
                    int j2 = l1 / 2;
                    int l2 = l1 % 2;
                    i1 = 54 + j2 * 54;
                    j1 = 6 + l2 * 27;
                } else if (l >= 0 && l < 5) {
                    i1 = -2000;
                    j1 = -2000;
                } else if (l == 45) {
                    i1 = 35;
                    j1 = 20;
                } else {
                    int k1 = l - 9;
                    int i2 = k1 % 9;
                    int k2 = k1 / 9;
                    i1 = 9 + i2 * 18;
                    if (l >= 36) {
                        j1 = 112;
                    } else {
                        j1 = 54 + k2 * 18;
                    }
                }

                Slot slot = new JourneyModeDuplicationScreen.DuplicationSlot(container.inventorySlots.get(l), l, i1, j1);
                (this.container).inventorySlots.add(slot);
            }

            this.destroyItemSlot = new Slot(TMP_INVENTORY, 0, 173, 112);
            (this.container).inventorySlots.add(this.destroyItemSlot);
        } else if (i == ItemGroup.INVENTORY.getIndex()) {
            (this.container).inventorySlots.clear();
            (this.container).inventorySlots.addAll(this.originalSlots);
            this.originalSlots = null;
        }

        if (this.searchField != null) {
            if (tab.hasSearchBar()) {
                this.searchField.setVisible(true);
                this.searchField.setCanLoseFocus(false);
                this.searchField.setFocused2(true);
                if (i != tab.getIndex()) {
                    this.searchField.setText("");
                }
                this.searchField.setWidth(tab.getSearchbarWidth());
                this.searchField.x = this.guiLeft + (82 /*default left*/ + 89 /*default width*/) - this.searchField.getWidth();

                this.updateDuplicationSearch();
            } else {
                this.searchField.setVisible(false);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setFocused2(false);
                this.searchField.setText("");
            }
        }

        this.currentScroll = 0.0F;
        this.container.scrollTo(0.0F);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (!this.needsScrollBars()) {
            return false;
        } else {
            int i = ((this.container).itemList.size() + 9 - 1) / 9 - 5;
            this.currentScroll = (float)((double)this.currentScroll - delta / (double)i);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
            this.container.scrollTo(this.currentScroll);
            return true;
        }
    }

    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        boolean flag = mouseX < (double)guiLeftIn || mouseY < (double)guiTopIn || mouseX >= (double)(guiLeftIn + this.xSize) || mouseY >= (double)(guiTopIn + this.ySize);
        this.field_199506_G = flag && !this.isMouseOverGroup(ItemGroup.GROUPS[selectedTabIndex], mouseX, mouseY);
        return this.field_199506_G;
    }

    protected boolean func_195376_a(double p_195376_1_, double p_195376_3_) {
        int i = this.guiLeft;
        int j = this.guiTop;
        int k = i + 171;//175;
        int l = j + 22;//18;
        int i1 = k + 14;
        int j1 = l + 156;//112;
        return p_195376_1_ >= (double)k && p_195376_3_ >= (double)l && p_195376_1_ < (double)i1 && p_195376_3_ < (double)j1;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.isScrolling) {
            int i = this.guiTop + 18;
            int j = i + 156;//112;
            this.currentScroll = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
            this.container.scrollTo(this.currentScroll);
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        int start = tabPage * 10;
        int end = Math.min(ItemGroup.GROUPS.length, ((tabPage + 1) * 10) + 2);
        if (tabPage != 0) start += 2;
        boolean rendered = false;

        for (int x = start; x < end; x++) {
            ItemGroup itemgroup = ItemGroup.GROUPS[x];
            if (itemgroup != null && this.func_238809_a_(matrixStack, itemgroup, mouseX, mouseY)) {
                rendered = true;
                break;
            }
        }
        if (!rendered && !this.func_238809_a_(matrixStack, ItemGroup.SEARCH, mouseX, mouseY))
            this.func_238809_a_(matrixStack, ItemGroup.INVENTORY, mouseX, mouseY);

        if (this.destroyItemSlot != null && selectedTabIndex == ItemGroup.INVENTORY.getIndex() && this.isPointInRegion(this.destroyItemSlot.xPos, this.destroyItemSlot.yPos, 16, 16, (double)mouseX, (double)mouseY)) {
            this.renderTooltip(matrixStack, field_243345_D, mouseX, mouseY);
        }

        if (maxPages != 0) {
            ITextComponent page = new StringTextComponent(String.format("%d / %d", tabPage + 1, maxPages + 1));
            RenderSystem.disableLighting();
            this.setBlitOffset(300);
            this.itemRenderer.zLevel = 300.0F;
            font.drawTextWithShadow(matrixStack, page.func_241878_f(), guiLeft + (xSize / 2) - (font.getStringPropertyWidth(page) / 2), guiTop - 44, -1);
            this.setBlitOffset(0);
            this.itemRenderer.zLevel = 0.0F;
        }

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    protected void renderTooltip(MatrixStack matrixStack, ItemStack itemStack, int mouseX, int mouseY) {
        if (selectedTabIndex == ItemGroup.SEARCH.getIndex()) {
            List<ITextComponent> list = itemStack.getTooltip(this.minecraft.player, this.minecraft.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
            List<ITextComponent> list1 = Lists.newArrayList(list);
            Item item = itemStack.getItem();
            ItemGroup itemgroup = item.getGroup();
            if (itemgroup == null && item == Items.ENCHANTED_BOOK) {
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack);
                if (map.size() == 1) {
                    Enchantment enchantment = map.keySet().iterator().next();

                    for(ItemGroup itemgroup1 : ItemGroup.GROUPS) {
                        if (itemgroup1.hasRelevantEnchantmentType(enchantment.type)) {
                            itemgroup = itemgroup1;
                            break;
                        }
                    }
                }
            }

            this.tagSearchResults.forEach((p_214083_2_, p_214083_3_) -> {
                if (p_214083_3_.contains(item)) {
                    list1.add(1, (new StringTextComponent("#" + p_214083_2_)).mergeStyle(TextFormatting.DARK_PURPLE));
                }

            });
            if (itemgroup != null) {
                list1.add(1, itemgroup.getGroupName().deepCopy().mergeStyle(TextFormatting.BLUE));
            }

            net.minecraft.client.gui.FontRenderer font = itemStack.getItem().getFontRenderer(itemStack);
            net.minecraftforge.fml.client.gui.GuiUtils.preItemToolTip(itemStack);
            this.renderWrappedToolTip(matrixStack, list1, mouseX, mouseY, (font == null ? this.font : font));
            net.minecraftforge.fml.client.gui.GuiUtils.postItemToolTip();
        } else {
            super.renderTooltip(matrixStack, itemStack, mouseX, mouseY);
        }

    }

    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        ItemGroup itemgroup = ItemGroup.GROUPS[selectedTabIndex];

        int start = tabPage * 10;
        int end = Math.min(ItemGroup.GROUPS.length, ((tabPage + 1) * 10 + 2));
        if (tabPage != 0) start += 2;

        for (int idx = start; idx < end; idx++) {
            ItemGroup itemgroup1 = ItemGroup.GROUPS[idx];
            if (itemgroup1 != null && itemgroup1.getIndex() != selectedTabIndex) {
                this.minecraft.getTextureManager().bindTexture(itemgroup1.getTabsImage());
                this.func_238808_a_(matrixStack, itemgroup1);
            }
        }

        if (tabPage != 0) {
            if (itemgroup != ItemGroup.SEARCH) {
                this.minecraft.getTextureManager().bindTexture(ItemGroup.SEARCH.getTabsImage());
                func_238808_a_(matrixStack, ItemGroup.SEARCH);
            }
            if (itemgroup != ItemGroup.INVENTORY) {
                this.minecraft.getTextureManager().bindTexture(ItemGroup.INVENTORY.getTabsImage());
                func_238808_a_(matrixStack, ItemGroup.INVENTORY);
            }
        }

        this.minecraft.getTextureManager().bindTexture(this.BACKGROUND_TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.searchField.render(matrixStack, x, y, partialTicks);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.guiLeft + 171;//175;
        int j = this.guiTop + 22;//18;
        int k = j + 156;//112;
        //looks like this is the current tab
        this.minecraft.getTextureManager().bindTexture(itemgroup.getTabsImage());
        if (itemgroup.hasScrollbar()) {
            this.blit(matrixStack, i, j + (int)((float)(k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }

        if ((itemgroup == null || itemgroup.getTabPage() != tabPage) && (itemgroup != ItemGroup.SEARCH && itemgroup != ItemGroup.INVENTORY))
            return;

        this.func_238808_a_(matrixStack, itemgroup);
        if (itemgroup == ItemGroup.INVENTORY) {
            InventoryScreen.drawEntityOnScreen(this.guiLeft + 88, this.guiTop + 45, 20, (float)(this.guiLeft + 88 - x), (float)(this.guiTop + 45 - 30 - y), this.minecraft.player);
        }

    }

    protected boolean isMouseOverGroup(ItemGroup p_195375_1_, double p_195375_2_, double p_195375_4_) {
        if (p_195375_1_.getTabPage() != tabPage && p_195375_1_ != ItemGroup.SEARCH && p_195375_1_ != ItemGroup.INVENTORY) return false;
        int i = p_195375_1_.getColumn();
        int j = 28 * i;
        int k = 0;
        if (p_195375_1_.isAlignedRight()) {
            j = this.xSize - 28 * (6 - i) + 2;
        } else if (i > 0) {
            j += i;
        }

        if (p_195375_1_.isOnTopRow()) {
            k = k - 32;
        } else {
            k = k + this.ySize;
        }

        return p_195375_2_ >= (double)j && p_195375_2_ <= (double)(j + 28) && p_195375_4_ >= (double)k && p_195375_4_ <= (double)(k + 32);
    }

    protected boolean func_238809_a_(MatrixStack p_238809_1_, ItemGroup p_238809_2_, int p_238809_3_, int p_238809_4_) {
        int i = p_238809_2_.getColumn();
        int j = 28 * i;
        int k = 0;
        if (p_238809_2_.isAlignedRight()) {
            j = this.xSize - 28 * (6 - i) + 2;
        } else if (i > 0) {
            j += i;
        }

        if (p_238809_2_.isOnTopRow()) {
            k = k - 32;
        } else {
            k = k + this.ySize;
        }

        if (this.isPointInRegion(j + 3, k + 3, 23, 27, (double)p_238809_3_, (double)p_238809_4_)) {
            this.renderTooltip(p_238809_1_, p_238809_2_.getGroupName(), p_238809_3_, p_238809_4_);
            return true;
        } else {
            return false;
        }
    }

    protected void func_238808_a_(MatrixStack p_238808_1_, ItemGroup p_238808_2_) {
        boolean flag = p_238808_2_.getIndex() == selectedTabIndex;
        boolean flag1 = p_238808_2_.isOnTopRow();
        int i = p_238808_2_.getColumn();
        int j = i * 28;
        int k = 0;
        int l = this.guiLeft + 28 * i;
        int i1 = this.guiTop;
        int j1 = 32;
        if (flag) {
            k += 32;
        }

        if (p_238808_2_.isAlignedRight()) {
            l = this.guiLeft + this.xSize - 28 * (6 - i);
        } else if (i > 0) {
            l += i;
        }

        if (flag1) {
            i1 = i1 - 28;
        } else {
            k += 64;
            i1 = i1 + (this.ySize - 4);
        }

        RenderSystem.color3f(1F, 1F, 1F); //Forge: Reset color in case Items change it.
        RenderSystem.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
        this.blit(p_238808_1_, l, i1, j, k, 28, 32);
        this.itemRenderer.zLevel = 100.0F;
        l = l + 6;
        i1 = i1 + 8 + (flag1 ? 1 : -1);
        RenderSystem.enableRescaleNormal();
        ItemStack itemstack = p_238808_2_.getIcon();
        this.itemRenderer.renderItemAndEffectIntoGUI(itemstack, l, i1);
        this.itemRenderer.renderItemOverlays(this.font, itemstack, l, i1);
        this.itemRenderer.zLevel = 0.0F;
    }

    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class Button extends AbstractButton {
        public JourneyModeDuplicationScreen screen;
        private boolean selected;
        public boolean pressed = false;

        protected Button(int x, int y) {
            super(x, y, 59, 21, StringTextComponent.EMPTY);
        }

        public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            Minecraft.getInstance().getTextureManager().bindTexture(JourneyModeDuplicationScreen.BACKGROUND_TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 228;
            int j = 77;
            if (this.pressed) {
                j += 59;
            }

            this.blit(matrixStack, this.x, this.y, j, i, this.width, this.height);
            this.func_230454_a_(matrixStack);
        }

        protected abstract void func_230454_a_(MatrixStack p_230454_1_);

        public boolean isSelected() {
            return this.selected;
        }

        public void setSelected(boolean selectedIn) {
            this.selected = selectedIn;
        }
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class Tab extends AbstractButton {
        public boolean currentTab = false;

        protected Tab(int x, int y) { super(x, y, 32, 28, StringTextComponent.EMPTY);}

        public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            Minecraft.getInstance().getTextureManager().bindTexture(JourneyModeDuplicationScreen.BACKGROUND_TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 221;
            int j = 0;
            if (!this.currentTab) {
                j += 32;
            }

            this.blit(matrixStack, this.x, this.y, j, i, this.width, this.height);
            this.func_230454_a_(matrixStack);
        }

        protected abstract void func_230454_a_(MatrixStack p_230454_1_);
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class SpriteTab extends JourneyModeDuplicationScreen.Tab {
        private final int u;
        private final int v;

        protected SpriteTab(int x, int y, int u, int v) {
            super(x, y);
            this.u = u;
            this.v = v;
        }

        protected void func_230454_a_(MatrixStack p_230454_1_) {
            this.blit(p_230454_1_, this.x + 7, this.y + 5, this.u, this.v, 18, 18);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class PowersTab extends JourneyModeDuplicationScreen.SpriteTab {
        public PowersTab(int x, int y) {
            super(x, y, 162, 202);
            this.currentTab = false;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(playerInventory.player.getUniqueID().toString(), "powers"));
        }

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModeDuplicationScreen.this.renderTooltip(matrixStack, POWERS_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class ResearchTab extends JourneyModeDuplicationScreen.SpriteTab {
        public ResearchTab(int x, int y) {
            super(x, y, 198, 184);
            this.currentTab = false;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(playerInventory.player.getUniqueID().toString(), "research"));
        }

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModeDuplicationScreen.this.renderTooltip(matrixStack, RESEARCH_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class DuplicationTab extends JourneyModeDuplicationScreen.SpriteTab {
        public DuplicationTab(int x, int y) {
            super(x, y, 198, 202);
            this.currentTab = true;
        }

        public void onPress() {
            //current tab, so nothing happens
        }

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModeDuplicationScreen.this.renderTooltip(matrixStack, DUPLICATION_TAB, mouseX, mouseY);
        }
    }

    //@OnlyIn(Dist.CLIENT)
    public static class DuplicationContainer extends Container {
        public final NonNullList<ItemStack> itemList = NonNullList.create();

        public DuplicationContainer(PlayerEntity player) {
            super((ContainerType<?>) null, 0);
            PlayerInventory playerInventory = player.inventory;

            int startX = 8;
            int startY = 18;
            int slotSizePlus2 = 18;

            int startDupeInvY = 22;
            /*for (int row = 0; row < 3; ++row) {
                for (int col = 0; col < 9; ++col) {
                    this.addSlot(new Slot(playerInventory, 9 + (row * 9) + col, startX + (col * slotSizePlus2), startDupeInvY + (row * slotSizePlus2)));
                }
            }*/

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 9; j++) {
                    this.addSlot(new JourneyModeDuplicationScreen.LockedSlot(JourneyModeDuplicationScreen.TMP_INVENTORY, 9 + (i * 9) + j, startX + (j * slotSizePlus2), startDupeInvY + (i * slotSizePlus2)));
                }
            }

            int startPlayerInvY = startY * 5 + 12;
            for (int row = 0; row < 3; ++row) {
                for (int col = 0; col < 9; ++col) {
                    this.addSlot(new Slot(playerInventory, 9 + (row * 9) + col, startX + (col * slotSizePlus2), startPlayerInvY + (row * slotSizePlus2)));
                }
            }

            //Hotbar
            int hotbarY = startPlayerInvY + (startPlayerInvY / 2) + 7;
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col, startX + (col * slotSizePlus2), hotbarY));
            }

        }

        public boolean canInteractWith(PlayerEntity playerIn) {return true;}

        public void scrollTo(float pos) {
            int i = (this.itemList.size() + 9 - 1) / 9 - 5;
            int j = (int)((double)(pos * (float)i) + 0.50);
            if (j < 0) {
                j = 0;
            }

            for(int k = 0; k < 5; k++) {
                for(int l = 0; l < 9; l++) {
                    int i1 = l + (k + j) * 9;
                    if (i1 >= 9 && i1 < this.itemList.size()) {
                        JourneyModeDuplicationScreen.TMP_INVENTORY.setInventorySlotContents(l + k * 9, this.itemList.get(i1));
                    } else {
                        JourneyModeDuplicationScreen.TMP_INVENTORY.setInventorySlotContents(l + k * 9, ItemStack.EMPTY);
                    }
                }
            }
        }

        public boolean canScroll() {return this.itemList.size() > 36;}

        public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
            if (index >= this.inventorySlots.size() - 36 && index < this.inventorySlots.size()) {
                Slot slot = this.inventorySlots.get(index);
                if (slot != null && slot.getHasStack()) {
                    slot.putStack(ItemStack.EMPTY);
                }
            }
            return ItemStack.EMPTY;
        }

        public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
            return slotIn.inventory != JourneyModeDuplicationScreen.TMP_INVENTORY;
        }

        public boolean canDragIntoSlot(Slot slotIn) {return slotIn.inventory != JourneyModeDuplicationScreen.TMP_INVENTORY;}
    }

    @OnlyIn(Dist.CLIENT)
    static class DuplicationSlot extends Slot {
        private final Slot slot;

        public DuplicationSlot(Slot p_i229959_1_, int p_i229959_2_, int p_i229959_3_, int p_i229959_4_) {
            super(p_i229959_1_.inventory, p_i229959_2_, p_i229959_3_, p_i229959_4_);
            this.slot = p_i229959_1_;
        }

        public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
            return this.slot.onTake(thePlayer, stack);
        }

        public boolean isItemValid(ItemStack stack) { return this.slot.isItemValid(stack);}

        public ItemStack getStack() {return this.slot.getStack();}

        public boolean getHasStack() {return this.slot.getHasStack();}

        public void putStack(ItemStack stack) {this.slot.putStack(stack);}

        public void onSlotChanged() { this.slot.onSlotChanged(); }

        public int getSlotStackLimit() { return this.slot.getSlotStackLimit(); }

        public int getItemStackLimit(ItemStack stack) { return this.slot.getItemStackLimit(stack); }

        @Nullable
        public Pair<ResourceLocation, ResourceLocation> getBackground() {return this.slot.getBackground(); }

        public ItemStack decrStackSize(int amount) { return this.slot.decrStackSize(amount); }

        public boolean isEnabled() {return this.slot.isEnabled(); }

        public boolean canTakeStack(PlayerEntity playerIn) { return this.slot.canTakeStack(playerIn); }

        @Override
        public int getSlotIndex() { return this.slot.getSlotIndex(); }

        @Override
        public boolean isSameInventory(Slot other) { return this.slot.isSameInventory(other); }

        @Override
        public Slot setBackground(ResourceLocation atlas, ResourceLocation sprite) {
            this.slot.setBackground(atlas, sprite);
            return this;
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class LockedSlot extends Slot {
        public LockedSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        public boolean canTakeStack(PlayerEntity playerIn) {
            if (super.canTakeStack(playerIn) && this.getHasStack()) {
                return this.getStack().getChildTag("DuplicationLock") == null;
            } else {
                return !this.getHasStack();
            }
        }
    }
}