package org.gneisscode.improvedmapcolors.resourcestuff;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.gneisscode.improvedmapcolors.PresetManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PresetLister extends SimplePreparableReloadListener<Map<PresetManager.Preset, Resource>> {

    public static PresetLister LISTENER = new PresetLister();
    public static void scanDirectory(
            ResourceManager resourceManager, FileToIdConverter fileToIdConverter, Map<PresetManager.Preset, Resource> map
    ) {
        for (Map.Entry<Identifier, Resource> entry : fileToIdConverter.listMatchingResources(resourceManager).entrySet()) {
            Identifier resourceLocation = entry.getKey();
            Identifier resourceLocation2 = fileToIdConverter.fileToId(resourceLocation);

//            LogUtils.getLogger().info("Preset: {}", resourceLocation2);
//            LogUtils.getLogger().info("key: {}", entry.getKey());

            PresetManager.Preset p = PresetManager.Preset.getFromSerializedName(resourceLocation.getPath().split("/")[1].replace(".zip", ""));

            map.put(p, entry.getValue());
        }


    }

    @Override
    protected @NotNull Map<PresetManager.Preset, Resource> prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {

        Map<PresetManager.Preset, Resource> map = new HashMap<>();
        scanDirectory(resourceManager, new FileToIdConverter("preset", ".zip"), map);
        return map;
    }

    @Override
    protected void apply(Map<PresetManager.Preset, Resource> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        for(PresetManager.Preset p : map.keySet()){
            PresetManager.addPack(p, map.get(p));
        }
    }

}
