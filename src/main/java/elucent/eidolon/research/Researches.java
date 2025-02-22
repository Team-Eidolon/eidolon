package elucent.eidolon.research;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import elucent.eidolon.Eidolon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

import static elucent.eidolon.util.RegistryUtil.getRegistryName;

public class Researches {
    static final Map<ResourceLocation, Research> researches = new HashMap<>();
    static final Multimap<ResourceLocation, Research> blockResearches = HashMultimap.create();
    static final Multimap<ResourceLocation, Research> entityResearches = HashMultimap.create();
    static final List<Function<Random, ResearchTask>> taskPool = new ArrayList<>();

    public static Research register(Research r, Object... sources) {
        researches.put(r.getRegistryName(), r);
        for (Object o : sources) {
            if (o instanceof Block b) {
                blockResearches.put(getRegistryName(b), r);
            } else if (o instanceof EntityType<?> e) {
                entityResearches.put(getRegistryName(e), r);
            }
        }
        return r;
    }

    public static void addTask(Function<Random, ResearchTask> task) {
        taskPool.add(task);
    }

    public static @NotNull Collection<Research> getBlockResearches(Block b) {
        return blockResearches.get(getRegistryName(b));
    }

    public static @NotNull Collection<Research> getEntityResearches(Entity e) {
        return entityResearches.get(getRegistryName(e.getType()));
    }

    public static Collection<Research> getResearches() {
        return researches.values();
    }

    @Nullable
    public static Research find(ResourceLocation location) {
        return researches.getOrDefault(location, null);
    }
    
    public static ResearchTask getRandomTask(Random random) {
        return taskPool.get(random.nextInt(taskPool.size())).apply(random);
    }
    
    public static void init() {
        addTask(ResearchTask.ScrivenerItems::new);
        addTask(ResearchTask.ScrivenerItems::new);
        addTask(ResearchTask.ScrivenerItems::new);
        addTask(ResearchTask.ScrivenerItems::new);
        addTask(ResearchTask.XP::new);
        addTask(ResearchTask.XP::new);
        register(new Research(new ResourceLocation(Eidolon.MODID, "gluttony"), 5), EntityType.PIG);
        register(new Research(new ResourceLocation(Eidolon.MODID, "test_block_research"), 1), Blocks.IRON_BLOCK);
    }
}
