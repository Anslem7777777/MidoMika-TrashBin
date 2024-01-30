package me.mika.midomikatrashbin.Utils;

import me.mika.midomikatrashbin.MidoMika_TrashBin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TrashBinUtil {
    private static NamespacedKey TrashBinKey;
    private ItemStack TrashBin;

    public void registerTrashBin() {
        TrashBin = createTrashBin();
        TrashBinKey = new NamespacedKey(MidoMika_TrashBin.getInstance(), "trash_bin");
        System.out.println("registerTrashBin");
        ShapedRecipe trashBinRecipe = new ShapedRecipe(TrashBinKey, TrashBin);
        trashBinRecipe.shape("LLL", "LBL", "LLL");
//        swordRecipe.setIngredient('X', Material.AIR);
        trashBinRecipe.setIngredient('L', Material.LAVA_BUCKET);
        trashBinRecipe.setIngredient('B', Material.BARREL);

        // Add the recipe
        Bukkit.getServer().addRecipe(trashBinRecipe);
    }

    public ItemStack createTrashBin() {
        System.out.println("createTrashBin");
        // 这里需要根据实际情况创建一个自定义剑的 ItemStack
        ItemStack trashBin = new ItemStack(Material.BARREL);
        ItemMeta trashBinMeta = trashBin.getItemMeta();

        trashBinMeta.setDisplayName(ChatColor.RED + "Trash Bin");
        List<String> trashBinLore = new ArrayList<>();
        trashBinLore.add(" ");
        trashBinLore.add(ChatColor.YELLOW + "Put Item Into Trash Bin To Destroy");
        trashBinMeta.setLore(trashBinLore);
        // 添加不显示物品信息的标志
        trashBinMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        trashBin.setItemMeta(trashBinMeta);
        return trashBin;
    }

    public static NamespacedKey getTrashBinKey() {
        return TrashBinKey;

    }
}
