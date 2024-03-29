package net.minecraft.world.item.crafting;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public final class Ingredient implements Predicate<ItemStack> {
   public static final Ingredient EMPTY = new Ingredient(Stream.empty());
   private final Ingredient.Value[] values;
   @Nullable
   private ItemStack[] itemStacks;
   @Nullable
   private IntList stackingIds;

   private Ingredient(Stream<? extends Ingredient.Value> p_43907_) {
      this.values = p_43907_.toArray((p_43933_) -> {
         return new Ingredient.Value[p_43933_];
      });
   }

   public ItemStack[] getItems() {
      if (this.itemStacks == null) {
         this.itemStacks = Arrays.stream(this.values).flatMap((p_43916_) -> {
            return p_43916_.getItems().stream();
         }).distinct().toArray((p_43910_) -> {
            return new ItemStack[p_43910_];
         });
      }

      return this.itemStacks;
   }

   public boolean test(@Nullable ItemStack p_43914_) {
      if (p_43914_ == null) {
         return false;
      } else if (this.isEmpty()) {
         return p_43914_.isEmpty();
      } else {
         for(ItemStack itemstack : this.getItems()) {
            if (itemstack.is(p_43914_.getItem())) {
               return true;
            }
         }

         return false;
      }
   }

   public IntList getStackingIds() {
      if (this.stackingIds == null) {
         ItemStack[] aitemstack = this.getItems();
         this.stackingIds = new IntArrayList(aitemstack.length);

         for(ItemStack itemstack : aitemstack) {
            this.stackingIds.add(StackedContents.getStackingIndex(itemstack));
         }

         this.stackingIds.sort(IntComparators.NATURAL_COMPARATOR);
      }

      return this.stackingIds;
   }

   public void toNetwork(FriendlyByteBuf p_43924_) {
      p_43924_.writeCollection(Arrays.asList(this.getItems()), FriendlyByteBuf::writeItem);
   }

   public JsonElement toJson() {
      if (this.values.length == 1) {
         return this.values[0].serialize();
      } else {
         JsonArray jsonarray = new JsonArray();

         for(Ingredient.Value ingredient$value : this.values) {
            jsonarray.add(ingredient$value.serialize());
         }

         return jsonarray;
      }
   }

   public boolean isEmpty() {
      return this.values.length == 0;
   }

   private static Ingredient fromValues(Stream<? extends Ingredient.Value> p_43939_) {
      Ingredient ingredient = new Ingredient(p_43939_);
      return ingredient.isEmpty() ? EMPTY : ingredient;
   }

   public static Ingredient of() {
      return EMPTY;
   }

   public static Ingredient of(ItemLike... p_43930_) {
      return of(Arrays.stream(p_43930_).map(ItemStack::new));
   }

   public static Ingredient of(ItemStack... p_43928_) {
      return of(Arrays.stream(p_43928_));
   }

   public static Ingredient of(Stream<ItemStack> p_43922_) {
      return fromValues(p_43922_.filter((p_43944_) -> {
         return !p_43944_.isEmpty();
      }).map(Ingredient.ItemValue::new));
   }

   public static Ingredient of(TagKey<Item> p_204133_) {
      return fromValues(Stream.of(new Ingredient.TagValue(p_204133_)));
   }

   public static Ingredient fromNetwork(FriendlyByteBuf p_43941_) {
      return fromValues(p_43941_.<ItemStack>readList(FriendlyByteBuf::readItem).stream().map(Ingredient.ItemValue::new));
   }

   public static Ingredient fromJson(@Nullable JsonElement p_43918_) {
      if (p_43918_ != null && !p_43918_.isJsonNull()) {
         if (p_43918_.isJsonObject()) {
            return fromValues(Stream.of(valueFromJson(p_43918_.getAsJsonObject())));
         } else if (p_43918_.isJsonArray()) {
            JsonArray jsonarray = p_43918_.getAsJsonArray();
            if (jsonarray.size() == 0) {
               throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
            } else {
               return fromValues(StreamSupport.stream(jsonarray.spliterator(), false).map((p_151264_) -> {
                  return valueFromJson(GsonHelper.convertToJsonObject(p_151264_, "item"));
               }));
            }
         } else {
            throw new JsonSyntaxException("Expected item to be object or array of objects");
         }
      } else {
         throw new JsonSyntaxException("Item cannot be null");
      }
   }

   private static Ingredient.Value valueFromJson(JsonObject p_43920_) {
      if (p_43920_.has("item") && p_43920_.has("tag")) {
         throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
      } else if (p_43920_.has("item")) {
         Item item = ShapedRecipe.itemFromJson(p_43920_);
         return new Ingredient.ItemValue(new ItemStack(item));
      } else if (p_43920_.has("tag")) {
         ResourceLocation resourcelocation = new ResourceLocation(GsonHelper.getAsString(p_43920_, "tag"));
         TagKey<Item> tagkey = TagKey.create(Registries.ITEM, resourcelocation);
         return new Ingredient.TagValue(tagkey);
      } else {
         throw new JsonParseException("An ingredient entry needs either a tag or an item");
      }
   }

   static class ItemValue implements Ingredient.Value {
      private final ItemStack item;

      ItemValue(ItemStack p_43953_) {
         this.item = p_43953_;
      }

      public Collection<ItemStack> getItems() {
         return Collections.singleton(this.item);
      }

      public JsonObject serialize() {
         JsonObject jsonobject = new JsonObject();
         jsonobject.addProperty("item", BuiltInRegistries.ITEM.getKey(this.item.getItem()).toString());
         return jsonobject;
      }
   }

   static class TagValue implements Ingredient.Value {
      private final TagKey<Item> tag;

      TagValue(TagKey<Item> p_204135_) {
         this.tag = p_204135_;
      }

      public Collection<ItemStack> getItems() {
         List<ItemStack> list = Lists.newArrayList();

         for(Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(this.tag)) {
            list.add(new ItemStack(holder));
         }

         return list;
      }

      public JsonObject serialize() {
         JsonObject jsonobject = new JsonObject();
         jsonobject.addProperty("tag", this.tag.location().toString());
         return jsonobject;
      }
   }

   interface Value {
      Collection<ItemStack> getItems();

      JsonObject serialize();
   }
}