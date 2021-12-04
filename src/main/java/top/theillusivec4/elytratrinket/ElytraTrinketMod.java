/*
 * Copyright (c) 2019-2021 C4
 *
 * This file is part of Elytra Trinket, a mod made for Minecraft.
 *
 * Elytra Trinket is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Elytra Trinket is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Elytra Trinket.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.elytratrinket;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;
import net.minecraft.world.event.GameEvent;

import java.util.Optional;

public class ElytraTrinketMod implements ModInitializer {

  @Override
  public void onInitialize() {
    EntityElytraEvents.CUSTOM.register((entity, tickElytra) -> {
      Optional<TrinketComponent> trinketComponent = TrinketsApi.getTrinketComponent(entity);
      if (trinketComponent.isPresent()) {
        for (Pair<SlotReference, ItemStack> p : trinketComponent.get().getAllEquipped()) {
          ItemStack stack = p.getRight();
          if (stack.getItem() == Items.ELYTRA && ElytraItem.isUsable(stack)) {
            if (tickElytra) {
              int nextRoll = entity.getRoll() + 1;
              if (!entity.world.isClient && nextRoll % 10 == 0) {
                if ((nextRoll / 10) % 2 == 0) {
                  stack.damage(1, entity, e1 -> TrinketsApi.onTrinketBroken(stack, p.getLeft(), e1));
                }
                entity.emitGameEvent(GameEvent.ELYTRA_FREE_FALL);
              }
            }
            return true;
          }
        }
      }

      return false;
    });
  }
}
