package cga.exercise.components.animation

import cga.exercise.components.geometry.Transformable
import org.joml.Math
import org.joml.Vector3f

object Roaming
{
    open fun roam(thing: Transformable, td: Float, speed: Float = 4f, orientation: Int = 0)
    {
        var speedPush:Float
        var moving:Boolean=false
        var rushing:Boolean=false
        var rotation:Boolean=false
        var s=speed*10
        var xc=0
        var zc=0
        if(orientation==0)
        {
            xc=1
        }
        else if(orientation==1)
        {
            zc=1
        }
        else if(orientation==2)
        {
            xc=-1
        }
        else if(orientation==3)
        {
            zc=-1
        }

        // Bewegung
        if(Math.random()>0.999)
        {
            thing.rhDir= !thing.rhDir
        }
        if(Math.random()>0.999)
        {
            thing.rhRot= !thing.rhRot
        }
        moving=thing.rhDir
        rotation=thing.rhRot
        rushing = Math.random()>0.8
        if (rushing)
        {
            speedPush = 4f
        }
        else
        {
            speedPush = 1f
        }
        if (moving)
        {
            thing.translateLocal(Vector3f(xc*-1f * td * speedPush*s, 0f, zc*-1f * td * speedPush*s))
        }
        var a=Math.random()
        if(rotation&&a>0.9f)
        {
            thing.rotateAroundPoint(0f, Math.toRadians(5f*td*s),0f, thing.getPosition())
        }
        else if(a>0.9f)
        {
            thing.rotateAroundPoint(0f, Math.toRadians(-5f*td*s),0f, thing.getPosition())
        }
    }
}