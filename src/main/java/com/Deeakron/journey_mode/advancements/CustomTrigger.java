package com.Deeakron.journey_mode.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.Set;

public class CustomTrigger implements ICriterionTrigger<CustomTrigger.Instance> {
    private final ResourceLocation location = new ResourceLocation("journey_mode:item_researched");
    private final Map<PlayerAdvancements, CustomTrigger.Listeners> listeners = Maps.newHashMap();


    @Override
    public ResourceLocation getId() {
        return location;
    }

    @Override
    public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener) {
        CustomTrigger.Listeners listeners = this.listeners.computeIfAbsent(playerAdvancementsIn, Listeners::new);
        listeners.add(listener);
    }

    @Override
    public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<CustomTrigger.Instance> listener) {
        CustomTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                this.listeners.remove(listener);
            }
        }
    }

    @Override
    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }

    @Override
    public Instance deserialize(JsonObject object, ConditionArrayParser conditions) {
        EntityPredicate.AndPredicate player = EntityPredicate.AndPredicate.deserializeJSONObject(object, "player", conditions);
        return new CustomTrigger.Instance(player, location);
    }

    public void trigger(ServerPlayerEntity player) {
        CustomTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());
        if (listeners!= null) {
            listeners.trigger();
        }
    }

    public static class Instance extends CriterionInstance {
        public Instance(EntityPredicate.AndPredicate player, ResourceLocation location){
            super(location, player);
        }

        public boolean test(){
            return true;
        }
    }

    static class Listeners {
        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<CustomTrigger.Instance>> listeners = Sets.newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn){
            this.playerAdvancements = playerAdvancementsIn;
        }


        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(Listener<CustomTrigger.Instance> listener){
            this.listeners.add(listener);
        }

        public void remove(Listener<CustomTrigger.Instance> listener){
            this.listeners.remove(listener);
        }

        public void trigger(){
            for (Listener<CustomTrigger.Instance> listener : Lists.newArrayList(this.listeners)){
                listener.grantCriterion(this.playerAdvancements);
            }

        }
    }
}
