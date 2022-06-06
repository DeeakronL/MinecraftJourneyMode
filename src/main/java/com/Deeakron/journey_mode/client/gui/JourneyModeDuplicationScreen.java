package com.Deeakron.journey_mode.client.gui;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.init.ResearchList;
import com.Deeakron.journey_mode.client.event.MenuSwitchEvent;
import com.Deeakron.journey_mode.journey_mode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.searchtree.SearchTree;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.TooltipFlag;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.tags.ItemTags;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class JourneyModeDuplicationScreen extends AbstractContainerScreen<JourneyModeDuplicationScreen.DuplicationContainer> /*DisplayEffectsScreen<JourneyModeDuplicationScreen.DuplicationContainer>*/ {
    private static final ResourceLocation DUPLICATION_INVENTORY_TABS = new ResourceLocation(journey_mode.MODID,"textures/gui/jm_duplication_tabs.png");
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(journey_mode.MODID, "textures/gui/jm_duplication.png");
    private static final ResourceLocation BACKGROUND_SEARCH_TEXTURE = new ResourceLocation(journey_mode.MODID, "textures/gui/jm_duplication_search.png");
    public static final Component POWERS_TAB = new TranslatableComponent("journey_mode.gui.tabs.powers");
    public static final Component RESEARCH_TAB = new TranslatableComponent("journey_mode.gui.tabs.research");
    public static final Component DUPLICATION_TAB = new TranslatableComponent("journey_mode.gui.tabs.duplication");
    public static final Component RECIPES_TAB = new TranslatableComponent("journey_mode.gui.tabs.recipes");
    public static final Component FILTER_TAB_0 = new TranslatableComponent("journey_mode.gui.tabs.filters_0");
    public static final Component FILTER_TAB_1 = new TranslatableComponent("journey_mode.gui.tabs.filters_1");
    public static final Component FILTER_TAB_2 = new TranslatableComponent("journey_mode.gui.tabs.filters_2");
    private static final SimpleContainer TMP_INVENTORY = new SimpleContainer(45);
    private static final Component TRASH_SLOT_TOOLTIP = new TranslatableComponent("inventory.binSlot");
    private boolean wasCreative;
    private boolean wasGodMode;
    private boolean filter = false;
    private int filterContainer = 0;
    private int filterTabPage = 0;
    private NonNullList<ItemStack> searchList;
    private JourneyModeDuplicationScreen.FilterTab filterTab;

    private static int selectedTabIndex; // need to put something

    private float currentScroll;

    private boolean isScrolling;
    private EditBox searchField;
    @Nullable
    private List<Slot> originalSlots;
    @Nullable
    private Slot destroyItemSlot;
    private DuplicationListener listener;
    private boolean ignoreTextInput;
    private static int tabPage = 0;
    private int maxPages = 0;
    private boolean hasClickedOutside;
    private final Map<ResourceLocation, Tag<Item>> tagSearchResults = Maps.newTreeMap();
    private final ResearchList playerList;
    private CreativeModeTab[] itemGroupSmall = null;
    private int hotbarIndex;
    private int survivalInventoryIndex;
    private ServerPlayer serverPlayerEntity;
    private static boolean hasRecipes;

    public JourneyModeDuplicationScreen(Player player, boolean wasCreative, boolean wasGodMode, ServerPlayer serverPlayerEntity) {
        super (new JourneyModeDuplicationScreen.DuplicationContainer(player, journey_mode.tempList), player.getInventory(), TextComponent.EMPTY);
        player.containerMenu = this.menu;
        this.wasCreative = wasCreative;
        this.wasGodMode = wasGodMode;
        this.serverPlayerEntity = serverPlayerEntity;
        this.passEvents = true;
        this.imageWidth = 190;
        this.imageHeight = 183;
        this.playerList = journey_mode.tempList;
        this.hasRecipes = journey_mode.hasRecipes;
        this.itemGroupSmall = new CreativeModeTab[CreativeModeTab.TABS.length - 2];
        boolean hotbarPresent = false;
        boolean survivalInventoryPresent = false;
        for (int i = 0; i < CreativeModeTab.TABS.length; i++) {
            int index = i;
            if (hotbarPresent) {
                index -= 1;
            }
            if (survivalInventoryPresent) {
                index -= 1;
            }
            if (CreativeModeTab.TABS[i] == CreativeModeTab.TAB_HOTBAR) {
                hotbarPresent = true;
                hotbarIndex = i;
            } else if (CreativeModeTab.TABS[i] == CreativeModeTab.TAB_INVENTORY) {
                survivalInventoryPresent = true;
                survivalInventoryIndex = i;
            } else {
                itemGroupSmall[index] = CreativeModeTab.TABS[i];
            }
        }
    }

    public void containerTick() {
        if (this.searchField != null) {
            this.searchField.tick();
        }

    }

    protected void slotClicked(@Nullable Slot slotIn, int slotId, int mouseButton, ClickType type) {
        if (this.hasTmpInventory(slotIn)) {
            this.searchField.moveCursorToEnd();
            this.searchField.setHighlightPos(0);
        }

        boolean flag = type == ClickType.QUICK_MOVE;
        type = slotId == -999 && type == ClickType.PICKUP ? ClickType.THROW : type;
        if (slotIn == null &&  type != ClickType.QUICK_CRAFT) {
            Inventory playerInventory1 = this.minecraft.player.getInventory();
            // .getSelected used to be .getCarried, might need testing for this
            if (!playerInventory1.getSelected().isEmpty() && this.hasClickedOutside) {
                if (mouseButton == 0) {
                    this.minecraft.player.drop(playerInventory1.getSelected(), true);
                    this.minecraft.gameMode.handleCreativeModeItemDrop(playerInventory1.getSelected());
                    // .setPickedItem used to be .setCarried, might need testing for this
                    playerInventory1.setPickedItem(ItemStack.EMPTY);
                }

                if (mouseButton == 1) {
                    ItemStack itemStack6 = playerInventory1.getSelected().split(1);
                    this.minecraft.player.drop(itemStack6, true);
                    this.minecraft.gameMode.handleCreativeModeItemDrop(itemStack6);
                }
            }
        } else {
            if (slotIn != null && !slotIn.mayPickup(this.minecraft.player)) {
                return;
            }

            if (slotIn == this.destroyItemSlot && flag) {
                for (int j = 0; j < this.minecraft.player.inventoryMenu.getItems().size(); j++) {
                    this.minecraft.gameMode.handleCreativeModeItemAdd(ItemStack.EMPTY, j);
                }
            } else if (type != ClickType.QUICK_CRAFT && slotIn.container == TMP_INVENTORY) {
                Inventory playerInventory = this.minecraft.player.getInventory();
                ItemStack itemStack5 = playerInventory.getSelected();
                ItemStack itemStack7 = slotIn.getItem();
                if (type == ClickType.SWAP) {
                    return;
                }

                if (type == ClickType.CLONE) {
                    return;
                }

                if (type == ClickType.THROW) {
                    return;
                }

                if (!itemStack5.isEmpty() && !itemStack7.isEmpty() && itemStack5.sameItem(itemStack7) && ItemStack.tagMatches(itemStack5, itemStack7)) {
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
                    boolean success = false;
                    try {
                        String item = "\"" +slotIn.getItem().getItem().getRegistryName() + "\"";
                        int[] result = this.playerList.get(item);
                        if (result[0] == result[1]) {
                            success = true;
                        }
                    } catch (NullPointerException e) {

                    } catch (Error e) {

                    }
                    if (success) {
                        if (((LockedSlot) slotIn).remaining <= 0) {
                            playerInventory.setPickedItem(itemStack7.copy());
                            itemStack5 = playerInventory.getSelected();
                            if (flag) {
                                itemStack5.setCount(itemStack5.getMaxStackSize());
                            }

                        }
                    }
                } else if (mouseButton == 0) {
                    playerInventory.setPickedItem(ItemStack.EMPTY);
                } else {
                    playerInventory.getSelected().shrink(1);
                }
            } else if (this.menu != null && type != ClickType.CLONE && type != ClickType.SWAP && type != ClickType.THROW) {
                ItemStack itemStack3 = slotIn == null ? ItemStack.EMPTY : this.menu.getSlot(slotIn.index).getItem();
                this.menu.clicked(slotIn == null ? slotId : slotIn.index, mouseButton, type, this.minecraft.player);
                if (InventoryMenu.getQuickcraftHeader(mouseButton) == 2) {

                } else if (slotIn != null) {
                    ItemStack itemStack4 = this.menu.getSlot(slotIn.index).getItem();
                    this.minecraft.gameMode.handleCreativeModeItemAdd(itemStack4, slotIn.index - (this.menu).slots.size() + 9 + 36);
                    int i = 45 + mouseButton;
                    if (type == ClickType.THROW && !itemStack3.isEmpty()) {
                        ItemStack itemStack2 = itemStack3.copy();
                        itemStack2.setCount(mouseButton == 0 ? 1 : itemStack2.getMaxStackSize());
                        this.minecraft.player.drop(itemStack2, true);
                        this.minecraft.gameMode.handleCreativeModeItemDrop(itemStack2);
                    }
                }
                this.minecraft.player.inventoryMenu.broadcastChanges();
            }

        }

    }

    private boolean hasTmpInventory(@Nullable Slot slotIn) {
        return slotIn != null && slotIn.container == TMP_INVENTORY;
    }

    protected void init() {
        super.init();
        this.addWidget(new JourneyModeDuplicationScreen.PowersTab(this.leftPos -29, this.topPos + 21, this));
        this.addWidget(new JourneyModeDuplicationScreen.ResearchTab(this.leftPos -29, this.topPos + 50, this));
        this.addWidget(new JourneyModeDuplicationScreen.DuplicationTab(this.leftPos -29, this.topPos + 79, this));
        if (this.hasRecipes) {
            this.addWidget(new JourneyModeDuplicationScreen.RecipesTab(this.leftPos -29, this.topPos + 108, this));
        }
        this.filterTab = new JourneyModeDuplicationScreen.FilterTab(this.leftPos + imageWidth - 3, this.topPos + 79);
        this.addWidget(filterTab);
        int tabCount = this.itemGroupSmall.length;
        if (tabCount > 10) {
            //add new tab buttons
            maxPages = (int) Math.ceil((tabCount - 10) / 10D);
            addWidget(new net.minecraft.client.gui.widget.button.Button(leftPos -25,              topPos, 20, 20, new TextComponent("<"), b -> tabPage = Math.max(tabPage - 1, 0       )));
            addWidget(new net.minecraft.client.gui.widget.button.Button(leftPos + imageWidth +5, topPos, 20, 20, new TextComponent(">"), b -> tabPage = Math.min(tabPage + 1, maxPages)));

        }
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        // might need to change itemGroup.search
        this.searchField = new EditBox(this.font, this.leftPos + 81, this.topPos + 9, 80, 9, new TranslatableComponent("itemGroup.search"));
        this.searchField.setMaxLength(50);
        this.searchField.setBordered(false);
        this.searchField.setVisible(false);
        this.searchField.setTextColor(16777215);
        //this.children.add(this.searchField);
        int i = selectedTabIndex;
        selectedTabIndex = -1;
        this.setCurrentDuplicationTab(itemGroupSmall[i], i);
        this.minecraft.player.inventoryMenu.removeSlotListener(this.listener);
        this.listener = new DuplicationListener(this.minecraft);
        this.minecraft.player.inventoryMenu.addSlotListener(this.listener);
    }

    public void resize(Minecraft minecraft, int width, int height) {
        String s = this.searchField.getValue();
        this.init(minecraft, width, height);
        this.searchField.setValue(s);
        if (!this.searchField.getValue().isEmpty()) {
            this.updateDuplicationSearch();
        }
    }

    public void removed() {
        super.removed();
        if (this.minecraft.player != null && this.minecraft.player.getInventory() != null) {
            this.minecraft.player.inventoryMenu.removeSlotListener(this.listener);
        }
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
        if (!this.wasCreative) {
            this.serverPlayerEntity.setGameMode(GameType.SURVIVAL);
        }
        this.serverPlayerEntity.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).setGodMode(wasGodMode);
    }

    public boolean charTyped(char codePoint, int modifiers) {
        if (this.ignoreTextInput) {
            return false;
        } else if (!itemGroupSmall[selectedTabIndex].hasSearchBar()) {
            return false;
        } else {
            String s = this.searchField.getValue();
            if (this.searchField.charTyped(codePoint, modifiers)) {
                if (!Objects.equals(s, this.searchField.getValue())) {
                    this.updateDuplicationSearch();
                }

                return true;
            } else {
                return false;
            }
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.ignoreTextInput = false;
        if (!itemGroupSmall[selectedTabIndex].hasSearchBar()) {
            if (this.minecraft.options.keyChat.matches(keyCode, scanCode)) {
                this.ignoreTextInput = true;
                this.setCurrentDuplicationTab(CreativeModeTab.TAB_SEARCH, 4);
                return true;
            } else {
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
        } else {
            boolean flag = !this.hasTmpInventory(this.hoveredSlot) || this.hoveredSlot.hasItem();
            boolean flag1 = InputConstants.getKey(keyCode, scanCode).getNumericKeyValue().isPresent();
            if (flag && flag1 && this.checkHotbarKeyPressed(keyCode, scanCode)) {
                this.ignoreTextInput = true;
                return true;
            } else {
                String s = this.searchField.getValue();
                if (this.searchField.keyPressed(keyCode, scanCode, modifiers)) {
                    if (!Objects.equals(s, this.searchField.getValue())) {
                        this.updateDuplicationSearch();
                    }

                    return true;
                } else {
                    return this.searchField.isFocused() && this.searchField.isVisible() && keyCode != 256 ? true : super.keyPressed(keyCode, scanCode, modifiers);
                }
            }
        }
    }

    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        this.ignoreTextInput = false;
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void updateDuplicationSearch() {
        (this.menu).itemList.clear();
        this.tagSearchResults.clear();

        CreativeModeTab tab = itemGroupSmall[selectedTabIndex];
        if (tab.hasSearchBar() && tab != CreativeModeTab.TAB_SEARCH) {
            tab.fillItemList(menu.itemList);
            if (!this.searchField.getValue().isEmpty()) {
                String search = this.searchField.getValue().toLowerCase(Locale.ROOT);
                java.util.Iterator<ItemStack> itr = menu.itemList.iterator();
                while (itr.hasNext()) {
                    ItemStack stack = itr.next();
                    boolean matches = false;
                    for (Component line : stack.getTooltipLines(this.minecraft.player, this.minecraft.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL)) {
                        if (ChatFormatting.stripFormatting(line.getString()).toLowerCase(Locale.ROOT).contains(search)) {
                            matches = true;
                            break;
                        }
                    }
                    if (!matches)
                        itr.remove();
                }
            }
            this.currentScroll = 0.0F;
            menu.scrollTo(0.0F);
            this.searchList = this.menu.itemList;
            return;
        }

        String s = this.searchField.getValue();
        if (s.isEmpty()) {
            for(Item item : Registry.ITEM) {
                item.fillItemCategory(CreativeModeTab.TAB_SEARCH, (this.menu).itemList);
            }
        } else {
            SearchTree<ItemStack> searchTree;
            if (s.startsWith("#")) {
                s = s.substring(1);
                searchTree = this.minecraft.getSearchTree(SearchRegistry.CREATIVE_TAGS);
                this.searchTags(s);
            } else {
                searchTree = this.minecraft.getSearchTree(SearchRegistry.CREATIVE_NAMES);
            }

            (this.menu).itemList.addAll(searchTree.search(s.toLowerCase(Locale.ROOT)));
        }

        this.currentScroll = 0.0F;
        this.menu.scrollTo(0.0F);
        this.filterTab.setUV(198, 220);
        this.filterTab.resetFilter();
        this.searchList = this.menu.itemList;
    }

    private int updateLockedFilter(int filter, NonNullList<ItemStack> current_item_list) {
        (this.menu).itemList.clear();
        CreativeModeTab tab = itemGroupSmall[selectedTabIndex];
        if (tab == CreativeModeTab.TAB_SEARCH && this.searchList != null) {
            current_item_list = this.searchList;
        }
        if (current_item_list == null) {
            current_item_list = menu.itemList;
        }
        if(filterTabPage != selectedTabIndex) {
            filter = 1;
        }
        if (true) {
            if (tab == CreativeModeTab.TAB_SEARCH) {
                String s = this.searchField.getValue();
                if (s.isEmpty()) {
                    for(Item item : Registry.ITEM) {
                        item.fillItemCategory(CreativeModeTab.TAB_SEARCH, current_item_list);
                    }
                } else {
                    SearchTree<ItemStack> searchTree;
                    if (s.startsWith("#")) {
                        s = s.substring(1);
                        searchTree = this.minecraft.getSearchTree(SearchRegistry.CREATIVE_TAGS);
                        this.searchTags(s);
                    } else {
                        searchTree = this.minecraft.getSearchTree(SearchRegistry.CREATIVE_NAMES);
                    }

                    current_item_list.addAll(searchTree.search(s.toLowerCase(Locale.ROOT)));
                }
            } else {
                tab.fillItemList(current_item_list);
            }

                java.util.Iterator<ItemStack> itr = current_item_list.iterator();
                while (itr.hasNext()) {
                    ItemStack stack = itr.next();

                    boolean success = false;
                    try {
                        String item = "\"" + stack.getItem().getRegistryName() + "\"";
                        if (filter == 1) {
                            if (this.menu.research.reachCap(item)) {
                                    itr.remove();
                            }

                        } else if (filter == 2) {
                            if (!this.menu.research.reachCap(item)) {
                                itr.remove();
                            }
                        }

                        success = true;
                    } catch (ClassCastException e) {
                        success = false;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        success = false;
                    } catch (IndexOutOfBoundsException e) {
                        success = false;
                    } catch (NullPointerException e) {
                        success = false;
                        itr.remove();
                    }

                }
            this.currentScroll = 0.0F;
            menu.scrollTo(0.0F);
            this.filterTabPage = selectedTabIndex;
            this.filterContainer = filter;
            return filter;
        }

        this.currentScroll = 0.0F;
        this.menu.scrollTo(0.0F);
        this.filterContainer = filter;
        return filter;
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

        TagCollection<Item> tagCollection = ItemTags.getAllTags();
        tagCollection.getAvailableTags().stream().filter(predicate).forEach((p_214082_2_) -> {
            Tag itag = this.tagSearchResults.put(p_214082_2_, tagCollection.getTag(p_214082_2_));
        });
    }

    protected void renderLabels(PoseStack PoseStack, int x, int y) {
        CreativeModeTab itemgroup = itemGroupSmall[selectedTabIndex];
        if (itemgroup != null && itemgroup.showTitle()) {
            RenderSystem.disableBlend();
            this.font.draw(PoseStack, itemgroup.getDisplayName(), 8.0F, 10.0F, itemgroup.getLabelColor());
        }

        for(Widget widget : this.renderables) {
            if (widget instanceof JourneyModePowersScreen.Button) {
                if (((JourneyModePowersScreen.Button) widget).isHovered()) {
                    widget.render(PoseStack, x - this.leftPos, y - this.topPos, 0);
                    break;
                }
            }
        }

    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            double d0 = mouseX - (double)this.leftPos;
            double d1 = mouseY - (double)this.topPos;

            for(CreativeModeTab itemgroup : itemGroupSmall) {
                if (itemgroup != null && this.isMouseOverGroup(itemgroup, d0, d1)) {
                    return true;
                }
            }

            if (this.insideScrollbar(mouseX, mouseY)) {
                this.isScrolling = this.needsScrollBars();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            double d0 = mouseX - (double)this.leftPos;
            double d1 = mouseY - (double)this.topPos;
            this.isScrolling = false;
            int index = 0;
            for(CreativeModeTab itemgroup : itemGroupSmall) {
                if (itemgroup.getId() >= survivalInventoryIndex) {
                    index = itemgroup.getId() - 2;
                } else if (itemgroup.getId() >= hotbarIndex){
                    index = itemgroup.getId() - 1;
                } else {
                    index = itemgroup.getId();
                }
                if (itemgroup != null && this.isMouseOverGroup(itemgroup, d0, d1)) {
                    this.setCurrentDuplicationTab(itemgroup, index);
                    return true;
                }
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean needsScrollBars() {
        if (itemGroupSmall[selectedTabIndex] == null) return false;
        return itemGroupSmall[selectedTabIndex].canScroll() && this.menu.canScroll();
    }

    private void setCurrentDuplicationTab(CreativeModeTab tab, int index) {
        if (tab == null) return;
        int i = selectedTabIndex;
        selectedTabIndex = index;
        slotColor = tab.getSlotColor();
        this.quickCraftSlots.clear();
        (this.menu).itemList.clear();
        if (tab != CreativeModeTab.TAB_SEARCH) {
            tab.fillItemList((this.menu).itemList);
        }

        if (tab == CreativeModeTab.TAB_INVENTORY) {
            InventoryMenu container = this.minecraft.player.inventoryMenu;
            if (this.originalSlots == null) {
                this.originalSlots = ImmutableList.copyOf((this.menu).slots);
            }

            (this.menu).slots.clear();

            for(int l = 0; l < container.slots.size(); ++l) {
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

                Slot slot = new JourneyModeDuplicationScreen.DuplicationSlot(container.slots.get(l), l, i1, j1);
                (this.menu).slots.add(slot);
            }

            this.destroyItemSlot = new Slot(TMP_INVENTORY, 0, 173, 112);
            (this.menu).slots.add(this.destroyItemSlot);
        }

        if (this.searchField != null) {
            if (tab.hasSearchBar()) {
                this.searchField.setVisible(true);
                this.searchField.setCanLoseFocus(false);
                this.searchField.setFocus(true);
                if (i != index) {
                    this.searchField.setValue("");
                }
                this.searchField.setWidth(tab.getSearchbarWidth());
                this.searchField.x = this.leftPos + (82 /*default left*/ + 89 /*default width*/) - this.searchField.getWidth();

                this.updateDuplicationSearch();
            } else {
                this.searchField.setVisible(false);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setFocus(false);
                this.searchField.setValue("");
            }
        }

        this.currentScroll = 0.0F;
        this.menu.scrollTo(0.0F);
        this.filterTab.setUV(198, 220);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (!this.needsScrollBars()) {
            return false;
        } else {
            int i = ((this.menu).itemList.size() + 9 - 1) / 9 - 5;
            this.currentScroll = (float)((double)this.currentScroll - delta / (double)i);
            this.currentScroll = Mth.clamp(this.currentScroll, 0.0F, 1.0F);
            this.menu.scrollTo(this.currentScroll);
            return true;
        }
    }

    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        boolean flag = mouseX < (double)guiLeftIn || mouseY < (double)guiTopIn || mouseX >= (double)(guiLeftIn + this.imageWidth) || mouseY >= (double)(guiTopIn + this.imageHeight);
        this.hasClickedOutside = flag && !this.isMouseOverGroup(itemGroupSmall[selectedTabIndex], mouseX, mouseY);
        return this.hasClickedOutside;
    }

    protected boolean insideScrollbar(double p_195376_1_, double p_195376_3_) {
        int i = this.leftPos;
        int j = this.topPos;
        int k = i + 171;
        int l = j + 22;
        int i1 = k + 14;
        int j1 = l + 156;
        return p_195376_1_ >= (double)k && p_195376_3_ >= (double)l && p_195376_1_ < (double)i1 && p_195376_3_ < (double)j1;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.isScrolling) {
            int i = this.topPos + 18;
            int j = i + 156;
            this.currentScroll = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.currentScroll = Mth.clamp(this.currentScroll, 0.0F, 1.0F);
            this.menu.scrollTo(this.currentScroll);
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(PoseStack);
        super.render(PoseStack, mouseX, mouseY, partialTicks);

        this.menu.render(PoseStack, this.topPos, this.leftPos, this.font);

        int start = tabPage * 8;
        int end = Math.min(itemGroupSmall.length, ((tabPage + 1) * 8) + 2);
        if (tabPage != 0) start += 2;
        boolean rendered = false;

        for (int x = start; x < end; x++) {
            CreativeModeTab itemgroup = itemGroupSmall[x];
            if (itemgroup != null && this.checkTabHovering(PoseStack, itemgroup, mouseX, mouseY)) {
                rendered = true;
                break;
            }
        }
        if (!rendered && !this.checkTabHovering(PoseStack, CreativeModeTab.TAB_SEARCH, mouseX, mouseY))
            this.checkTabHovering(PoseStack, CreativeModeTab.TAB_INVENTORY, mouseX, mouseY);

        if (maxPages != 0) {
            Component page = new TextComponent(String.format("%d / %d", tabPage + 1, maxPages + 1));
            RenderSystem.disableLighting();
            this.setBlitOffset(300);
            this.itemRenderer.blitOffset = 300.0F;
            font.drawShadow(PoseStack, page.getVisualOrderText(), leftPos + (imageWidth + 45) - (font.width(page) / 2), topPos +7, -1);
            this.setBlitOffset(0);
            this.itemRenderer.blitOffset = 0.0F;
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderTooltip(PoseStack, mouseX, mouseY);
    }

    protected void renderTooltip(PoseStack PoseStack, ItemStack itemStack, int mouseX, int mouseY) {
        if (selectedTabIndex == CreativeModeTab.TAB_SEARCH.getId()) {
            List<Component> list = itemStack.getTooltipLines(this.minecraft.player, this.minecraft.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
            List<Component> list1 = Lists.newArrayList(list);
            Item item = itemStack.getItem();
            CreativeModeTab itemgroup = item.getItemCategory();
            if (itemgroup == null && item == Items.ENCHANTED_BOOK) {
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack);
                if (map.size() == 1) {
                    Enchantment enchantment = map.keySet().iterator().next();

                    for(CreativeModeTab itemgroup1 : itemGroupSmall) {
                        if (itemgroup1.hasEnchantmentCategory(enchantment.category)) {
                            itemgroup = itemgroup1;
                            break;
                        }
                    }
                }
            }

            this.tagSearchResults.forEach((p_214083_2_, p_214083_3_) -> {
                if (p_214083_3_.contains(item)) {
                    list1.add(1, (new TextComponent("#" + p_214083_2_)).withStyle(ChatFormatting.DARK_PURPLE));
                }

            });
            if (itemgroup != null && itemgroup != CreativeModeTab.TAB_INVENTORY) {
                list1.add(1, itemgroup.getDisplayName().copy().withStyle(ChatFormatting.BLUE));
            }

            net.minecraft.client.gui.Font font = itemStack.getItem().getFont(itemStack);
            net.minecraftforge.fml.client.gui.GuiUtils.preItemToolTip(itemStack);
            this.renderWrappedToolTip(PoseStack, list1, mouseX, mouseY, (font == null ? this.font : font));
            net.minecraftforge.fml.client.gui.GuiUtils.postItemToolTip();
        } else {
            super.renderTooltip(PoseStack, itemStack, mouseX, mouseY);
        }

    }

    protected void renderBg(PoseStack PoseStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        CreativeModeTab itemgroup = itemGroupSmall[selectedTabIndex];

        int start = tabPage * 8;
        int end = Math.min(itemGroupSmall.length, ((tabPage + 1) * 8 + 2));
        if (tabPage != 0) start += 2;

        for (int idx = start; idx < end; idx++) {
            CreativeModeTab itemgroup1 = itemGroupSmall[idx];
            int index = 0;
            if (itemgroup1.getId() >= survivalInventoryIndex) {
                index = itemgroup1.getId() - 2;
            } else if (itemgroup1.getId() >= hotbarIndex){
                index = itemgroup1.getId() - 1;
            } else {
                index = itemgroup1.getId();
            }
            if (itemgroup1 != null && index != selectedTabIndex && itemgroup1 != CreativeModeTab.TAB_HOTBAR && itemgroup1 != CreativeModeTab.TAB_INVENTORY) {
                RenderSystem.setShaderTexture(0, DUPLICATION_INVENTORY_TABS);
                this.renderTabButton(PoseStack, itemgroup1);
            }
        }

        if (tabPage != 0) {
            if (itemgroup != CreativeModeTab.TAB_SEARCH  && itemgroup != CreativeModeTab.TAB_HOTBAR && itemgroup != CreativeModeTab.TAB_INVENTORY) {
                RenderSystem.setShaderTexture(0, DUPLICATION_INVENTORY_TABS);
                renderTabButton(PoseStack, CreativeModeTab.TAB_SEARCH);
            }
            if (itemgroup != CreativeModeTab.TAB_INVENTORY && itemgroup != CreativeModeTab.TAB_HOTBAR) {

            }
        }
        if (itemgroup != CreativeModeTab.TAB_SEARCH) {
            RenderSystem.setShaderTexture(0, this.BACKGROUND_TEXTURE);
        } else {
            RenderSystem.setShaderTexture(0, this.BACKGROUND_SEARCH_TEXTURE);
        }
        this.blit(PoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.searchField.render(PoseStack, x, y, partialTicks);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.leftPos + 171;
        int j = this.topPos + 22;
        int k = j + 156;
        //looks like this is the current tab
        RenderSystem.setShaderTexture(0, DUPLICATION_INVENTORY_TABS);
        if (itemgroup.canScroll()) {
            this.blit(PoseStack, i, j + (int)((float)(k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }

        if ((itemgroup == null || itemgroup.getTabPage() != tabPage) && itemgroup != CreativeModeTab.TAB_SEARCH && itemgroup != CreativeModeTab.TAB_INVENTORY  && itemgroup != CreativeModeTab.TAB_HOTBAR)
            return;

        this.renderTabButton(PoseStack, itemgroup);
        if (itemgroup == CreativeModeTab.TAB_INVENTORY) {
            InventoryScreen.renderEntityInInventory(this.leftPos + 88, this.topPos + 45, 20, (float)(this.leftPos + 88 - x), (float)(this.topPos + 45 - 30 - y), this.minecraft.player);
        }

    }

    protected boolean isMouseOverGroup(CreativeModeTab p_195375_1_, double p_195375_2_, double p_195375_4_) {
        if (p_195375_1_.getTabPage() != tabPage && p_195375_1_ != CreativeModeTab.TAB_SEARCH && p_195375_1_ != CreativeModeTab.TAB_INVENTORY){ return false;};
        int i = p_195375_1_.getColumn();
        int j = 28 * i;
        int k = 0;
        if (p_195375_1_.isAlignedRight()) {
            j = this.imageWidth - 28 * (6 - i) + 2;
        } else if (i > 0) {
            j += i;
        }

        if (p_195375_1_.isTopRow()) {
            k = k - 32;
        } else {
            k = k + this.imageHeight;
        }

        return p_195375_2_ >= (double)j && p_195375_2_ <= (double)(j + 28) && p_195375_4_ >= (double)k && p_195375_4_ <= (double)(k + 32);
    }

    protected boolean checkTabHovering(PoseStack p_238809_1_, CreativeModeTab p_238809_2_, int p_238809_3_, int p_238809_4_) {
        int i = p_238809_2_.getColumn();
        int j = 28 * i;
        int k = 0;
        if (p_238809_2_.isAlignedRight()) {
            j = this.imageWidth - 28 * (6 - i) + 2;
        } else if (i > 0) {
            j += i;
        }

        if (p_238809_2_.isTopRow()) {
            k = k - 32;
        } else {
            k = k + this.imageHeight;
        }

        if (this.isHovering(j + 3, k + 3, 23, 27, (double)p_238809_3_, (double)p_238809_4_)) {
            if (p_238809_2_ == CreativeModeTab.TAB_INVENTORY) {
                return false;
            }
            this.renderTooltip(p_238809_1_, p_238809_2_.getDisplayName(), p_238809_3_, p_238809_4_);
            return true;
        } else {
            return false;
        }
    }

    protected void renderTabButton(PoseStack p_238808_1_, CreativeModeTab p_238808_2_) {
        int index = p_238808_2_.getId();
        if (p_238808_2_.getId() >= hotbarIndex) {
            index = index - 1;
        }
        if (p_238808_2_.getId() >= survivalInventoryIndex) {
            index = index - 1;
        }
        boolean flag = index == selectedTabIndex;
        boolean flag1 = p_238808_2_.isTopRow();
        int i = p_238808_2_.getColumn();
        int j = i * 28;
        int k = 0;
        int l = this.leftPos + 28 * i;
        int i1 = this.topPos;
        int j1 = 32;
        if (flag) {
            k += 32;
        }

        if (p_238808_2_.isAlignedRight()) {
            l = this.leftPos + this.imageWidth - 28 * (6 - i);
        } else if (i > 0) {
            l += i;
        }

        if (flag1) {
            i1 = i1 - 28;
        } else {
            k += 64;
            i1 = i1 + (this.imageHeight - 4);
        }

        RenderSystem.color3f(1F, 1F, 1F); //Forge: Reset color in case Items change it.
        RenderSystem.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
        this.blit(p_238808_1_, l, i1, j, k, 28, 32);
        this.itemRenderer.blitOffset = 100.0F;
        l = l + 6;
        i1 = i1 + 8 + (flag1 ? 1 : -1);
        RenderSystem.enableRescaleNormal();
        ItemStack itemstack = p_238808_2_.getIconItem();
        this.itemRenderer.renderAndDecorateItem(itemstack, l, i1);
        this.itemRenderer.renderGuiItemDecorations(this.font, itemstack, l, i1);
        this.itemRenderer.blitOffset = 0.0F;
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
            super(x, y, 59, 21, TextComponent.EMPTY);
        }

        public void renderButton(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
            // Might not be right?
            RenderSystem.setShaderTexture(0, JourneyModeDuplicationScreen.BACKGROUND_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 228;
            int j = 77;
            if (this.pressed) {
                j += 59;
            }

            this.blit(PoseStack, this.x, this.y, j, i, this.width, this.height);
            this.renderIcon(PoseStack);
        }

        protected abstract void renderIcon(PoseStack p_230454_1_);

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
        public boolean filterTab = false;
        public JourneyModeDuplicationScreen screen;

        protected Tab(int x, int y) {
            super(x, y, 32, 28, TextComponent.EMPTY);
        }

        protected Tab(int x, int y, JourneyModeDuplicationScreen screen) {
            super(x, y, 32, 28, TextComponent.EMPTY);
            this.screen = screen;
        }

        public void renderButton(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
            RenderSystem.setShaderTexture(0, JourneyModeDuplicationScreen.BACKGROUND_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 221;
            int j = 0;
            if (!this.currentTab) {
                j += 32;
            }
            if (this.filterTab) {
                j += 64;
            }

            this.blit(PoseStack, this.x, this.y, j, i, this.width, this.height);
            this.renderIcon(PoseStack);
        }

        protected abstract void renderIcon(PoseStack p_230454_1_);
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class SpriteTab extends JourneyModeDuplicationScreen.Tab {
        private int u;
        private int v;

        protected SpriteTab(int x, int y, int u, int v) {
            super(x, y);
            this.u = u;
            this.v = v;
        }

        protected SpriteTab(int x, int y, int u, int v, JourneyModeDuplicationScreen screen) {
            super(x, y, screen);
            this.u = u;
            this.v = v;
        }

        protected void renderIcon(PoseStack p_230454_1_) {
            this.blit(p_230454_1_, this.x + 7, this.y + 5, this.u, this.v, 18, 18);
        }

        public void setUV(int u, int v) {
            this.u = u;
            this.v = v;
        }

        @Override
        public void updateNarration(NarrationElementOutput p_169152_) {

        }
    }

    @OnlyIn(Dist.CLIENT)
    class PowersTab extends JourneyModeDuplicationScreen.SpriteTab {
        public PowersTab(int x, int y, JourneyModeDuplicationScreen screen) {
            super(x, y, 162, 202, screen);
            this.currentTab = false;
        }

        public void onPress() {
            this.screen.minecraft.player.drop(inventory.getCarried(), true);
            this.screen.minecraft.gameMode.handleCreativeModeItemDrop(inventory.getCarried());
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(Minecraft.getInstance().player.getStringUUID(), "powers"));
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModeDuplicationScreen.this.renderTooltip(PoseStack, POWERS_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class ResearchTab extends JourneyModeDuplicationScreen.SpriteTab {
        public ResearchTab(int x, int y, JourneyModeDuplicationScreen screen) {
            super(x, y, 198, 184, screen);
            this.currentTab = false;
        }

        public void onPress() {
            this.screen.minecraft.player.drop(inventory.getCarried(), true);
            this.screen.minecraft.gameMode.handleCreativeModeItemDrop(inventory.getCarried());
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(Minecraft.getInstance().player.getStringUUID(), "research"));
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModeDuplicationScreen.this.renderTooltip(PoseStack, RESEARCH_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class DuplicationTab extends JourneyModeDuplicationScreen.SpriteTab {
        public DuplicationTab(int x, int y, JourneyModeDuplicationScreen screen) {
            super(x, y, 198, 202, screen);
            this.currentTab = true;
        }

        public void onPress() {
            //current tab, so nothing happens
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModeDuplicationScreen.this.renderTooltip(PoseStack, DUPLICATION_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class RecipesTab extends JourneyModeDuplicationScreen.SpriteTab {
        public RecipesTab(int x, int y, JourneyModeDuplicationScreen screen) {
            super(x, y, 217, 202, screen);
            this.currentTab = false;
        }

        public void onPress() {
            this.screen.minecraft.player.drop(inventory.getCarried(), true);
            this.screen.minecraft.gameMode.handleCreativeModeItemDrop(inventory.getCarried());
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(Minecraft.getInstance().player.getStringUUID(), "recipes"));
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModeDuplicationScreen.this.renderTooltip(PoseStack, RECIPES_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class FilterTab extends JourneyModeDuplicationScreen.SpriteTab {
        private int filter = 0;
        public FilterTab(int x, int y) {
            super(x, y, 198, 184);
            this.currentTab = true;
            this.filterTab = true;
        }

        public void onPress() {
            if (filter == 0) {
                filter = 1;
            } else if (filter == 1) {
                filter = 2;
            } else if (filter == 2) {
                filter = 0;
            }
            this.filter = updateLockedFilter(this.filter, null);
            if (filter == 0) {
                this.setUV(198, 220);
            } else if (filter == 1) {
                this.setUV(234, 220);
            } else if (filter == 2) {
                this.setUV(216, 220);
            }
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            if (this.filter == 0) {
                JourneyModeDuplicationScreen.this.renderTooltip(PoseStack, FILTER_TAB_0, mouseX, mouseY);
            } else if (this.filter == 1) {
                JourneyModeDuplicationScreen.this.renderTooltip(PoseStack, FILTER_TAB_1, mouseX, mouseY);
            } else if (this.filter == 2) {
                JourneyModeDuplicationScreen.this.renderTooltip(PoseStack, FILTER_TAB_2, mouseX, mouseY);
            }

        }

        public void resetFilter() {
            this.filter = 0;
        }

    }

    public static class DuplicationContainer extends AbstractContainerMenu {
        public final NonNullList<ItemStack> itemList = NonNullList.create();
        public ResearchList research;

        public DuplicationContainer(Player player, ResearchList research) {
            super((MenuType<?>) null, 0);
            Inventory playerInventory = player.inventory;
            this.research = research;

            int startX = 8;
            int startY = 18;
            int slotSizePlus2 = 18;

            int startDupeInvY = 22;

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 9; j++) {
                    boolean wasResearched = false;
                    int count = 0;
                    this.addSlot(new JourneyModeDuplicationScreen.LockedSlot(JourneyModeDuplicationScreen.TMP_INVENTORY, 9 + (i * 9) + j, startX + (j * slotSizePlus2), startDupeInvY + (i * slotSizePlus2), wasResearched, count));
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

        public boolean stillValid(Player playerIn) {return true;}

        public void scrollTo(float pos) {
            int i = (this.itemList.size() + 18 - 1) / 9 - 5;
            int j = (int)((double)(pos * (float)i) + 0.50);
            if (j < 0) {
                j = 0;
            }

            for(int k = 0; k < 5; k++) {
                for(int l = 0; l < 9; l++) {
                    int i1 = (l + (k + j) * 9) - 9;
                    if (i1 >= 0 && i1 < this.itemList.size()) {
                        JourneyModeDuplicationScreen.TMP_INVENTORY.setItem(l + k * 9, this.itemList.get(i1));
                        boolean success = false;
                        try {
                            LockedSlot slot = (LockedSlot) this.slots.get(l + k * 9);
                            String item = "\"" + itemList.get(i * 9 + j).getItem().getRegistryName() + "\"";
                            int[] result = this.research.get(item);
                            success = true;
                        } catch (ClassCastException e) {
                            success = false;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            success = false;
                        } catch (IndexOutOfBoundsException e) {
                            success = false;
                        } catch (NullPointerException e) {
                            success = false;
                        }
                        if (success) {
                            LockedSlot slot = (LockedSlot) this.slots.get(l + k * 9);
                            String item = "\"" + itemList.get(i * 9 + j).getItem().getRegistryName() + "\"";

                            int[] result = this.research.get(item);
                            boolean wasResearched = false;
                            int count = 0;
                            if (result[0] < result[1]) {
                                wasResearched = true;
                                count = 0;
                            } else {
                                wasResearched = false;
                                count = result[1] - result[0];
                            }
                            slot.changeResearch(wasResearched, count);
                        }
                    } else {
                        JourneyModeDuplicationScreen.TMP_INVENTORY.setItem(l + k * 9, ItemStack.EMPTY);
                    }
                }
            }
        }

        public boolean canScroll() {return this.itemList.size() > 36;}

        public ItemStack quickMoveStack(Player playerIn, int index) {
            if (index >= this.slots.size() - 45 && index < this.slots.size()) {
                Slot slot = this.slots.get(index);
                if (slot != null && slot.hasItem()) {
                    slot.set(ItemStack.EMPTY);
                }
            }
            return ItemStack.EMPTY;
        }

        public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
            return slotIn.container != JourneyModeDuplicationScreen.TMP_INVENTORY;
        }

        public boolean canDragTo(Slot slotIn) {return slotIn.container != JourneyModeDuplicationScreen.TMP_INVENTORY;}

        public void render(PoseStack matrix, int topX, int topY, Font font) {
            int baseX = topX + 32;
            int baseY = topY + 8;
            matrix.translate(0, 0, 300);
            matrix.scale((float)0.75, (float)0.75, 1);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 9; j++) {
                    try {
                        String item = "\"" + this.slots.get(i * 9 + j).getItem().getItem().getRegistryName() + "\"";
                        int[] result = this.research.get(item);
                        int diff = result[1] - result[0];
                        String string = Integer.toString(diff);
                        if (diff > 0) {
                            font.drawShadow(matrix, string, (float)((baseY + (j * 18))/0.75), (float)((baseX + (i * 18))/0.75), ChatFormatting.RED.getColor());
                        }
                    } catch (NullPointerException e) {
                        if (this.slots.get(i * 9 + j).hasItem()) {
                            String string = "  X  ";
                            font.drawShadow(matrix, string, (float)((baseY + (j * 18))/0.75), (float)((baseX + (i * 18))/0.75), ChatFormatting.RED.getColor());
                        }
                    }

                }
            }
            matrix.scale((float) (1/0.75), (float) (1/0.75), 1);
        }

    }

    @OnlyIn(Dist.CLIENT)
    static class DuplicationSlot extends Slot {
        private final Slot slot;

        public DuplicationSlot(Slot p_i229959_1_, int p_i229959_2_, int p_i229959_3_, int p_i229959_4_) {
            super(p_i229959_1_.container, p_i229959_2_, p_i229959_3_, p_i229959_4_);
            this.slot = p_i229959_1_;
        }

        public ItemStack onTake(Player thePlayer, ItemStack stack) {
            return this.slot.onTake(thePlayer, stack);
        }

        public boolean mayPlace(ItemStack stack) { return this.slot.mayPlace(stack);}

        public ItemStack getItem() {return this.slot.getItem();}

        public boolean hasItem() {return this.slot.hasItem();}

        public void set(ItemStack stack) {this.slot.set(stack);}

        public void setChanged() { this.slot.setChanged(); }

        public int getMaxStackSize() { return this.slot.getMaxStackSize(); }

        public int getMaxStackSize(ItemStack stack) { return this.slot.getMaxStackSize(stack); }

        @Nullable
        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {return this.slot.getNoItemIcon(); }

        public ItemStack remove(int amount) { return this.slot.remove(amount); }

        public boolean isActive() {return this.slot.isActive(); }

        public boolean mayPickup(Player playerIn) { return this.slot.mayPickup(playerIn); }

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
        public boolean researched;
        public int remaining;

        public LockedSlot(Container inventoryIn, int index, int xPosition, int yPosition, boolean research, int count) {
            super(inventoryIn, index, xPosition, yPosition);
            this.researched = research;
            this.remaining = count;
        }

        public boolean mayPickup(Player playerIn) {
            if (super.mayPickup(playerIn) && this.hasItem()) {
                return true;
            } else {
                return !this.hasItem();
            }
        }

        public void set(ItemStack stack) {}

        public void changeResearch(boolean research, int remaining) {
            this.researched = research;
            this.remaining = remaining;
        }
    }
}