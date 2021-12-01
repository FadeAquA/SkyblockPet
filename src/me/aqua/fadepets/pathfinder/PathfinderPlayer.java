package me.aqua.fadepets.pathfinder;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.navigation.Navigation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PathfinderPlayer extends PathfinderGoal {

    private final EntityInsentient entity;
    private final Player owner;
    private final float distanceSquared;
    private final Navigation navigation;

    public PathfinderPlayer(EntityInsentient entity, Player owner, float distance) {
        this.entity = entity;
        this.owner = owner;
        this.distanceSquared = distance * distance;
        this.navigation = (Navigation) entity.getNavigation();
    }

    @Override
    public boolean a() {
        if (owner != null && !owner.isDead() && owner.isOnline()) {
            Location loc = owner.getLocation();
            return entity.h(loc.getX(), loc.getY(), loc.getZ()) > (double) distanceSquared;
        }
        return false;
    }

    public boolean b() {
        return !navigation.m();
    }

    public void d() {
        entity.setNoAI(true);
    }
}

/**public class PathfinderGoalPet extends PathfinderGoal {

 private final EntityInsentient a; //pet
 private @Nullable EntityLiving b; //owner of pet

 private final double f; //pet speed
 private final float g; //distance between owner and pet

 private double c; //x
 private double d; //y
 private double e; //z

 public PathfinderGoalPet(EntityInsentient a, double speed, float distance) {
 this.a = a;
 this.f = speed;
 this.g = distance;
 this.a(EnumSet.of(Type.MOVE));
 }

 @Override
 public boolean a() {
 this.b = this.a.getGoalTarget();
 if (this.b == null)
 return false;
 else if (this.a.getDisplayName() == null)
 return false;
 else if (this.b.h(this.a.locX(), this.a.locY(), this.a.locZ()) > (double) (this.g * this.g)) {
 //teleport pet to owner
 a.setPosition(this.b.locX(), this.b.locY(), this.b.locZ());
 return false;
 }
 else {
 //follow the owner

 //Vec3D vec = RandomPositionGenerator.a(EntityCreature)this.a, 16, 7, this.b.getPositionVector());
 //if (vec == null)
 //  return false;
 this.c = this.b.locX();
 this.d = this.b.locY();
 this.e = this.b.locZ();
 return true;
 }
 }

 public void c() {
 //runner
 this.a.getNavigation().a(this.c, this.d, this.e, this.f);
 }

 public boolean b() {
 return !this.a.getNavigation().m() && this.b.h(this.a.locX(), this.a.locY(), this.a.locZ()) < (double) (this.g * this.g);
 }

 public void d() {
 this.b = null;
 }
 }
 **/
