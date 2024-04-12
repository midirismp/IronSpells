package io.redspace.ironsspellbooks.entity.mobs.goals;

import java.util.ArrayList;
import java.util.List;

public class AttackAnimationData {
    //public final int id;
    public final int lengthInTicks;
    public final List<Integer> attackTimestamps;

    public AttackAnimationData(int lengthInTicks, int... attackTimestamps) {
        //this.id = id;
        this.lengthInTicks = lengthInTicks;
        this.attackTimestamps = new ArrayList<>();
        for (int i : attackTimestamps)
            this.attackTimestamps.add(i);

    }

    /**
     * Returns for the tick when the animation should deal damage/hit. It is expected tickCount starts at the animation length and decreases
     */
    public boolean isHitFrame(int tickCount) {
        for (int i : attackTimestamps)
            if (tickCount == lengthInTicks - i)
                return true;
        return false;
    }
}
