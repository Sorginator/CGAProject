package cga.exercise.components.animation

import cga.exercise.components.geometry.Transformable
import cga.framework.GameWindow
import org.joml.Math
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW

object Flying
{
    open fun fly(thing:Transformable, td:Float, window:GameWindow)
    {
        var speedPush:Float
        var speed:Float=4f
        // Bewegung
        if (window.getKeyState(GLFW.GLFW_KEY_SPACE))
        {
            speedPush = 4f
        }
        else
        {
            speedPush = 1f
        }
        if (window.getKeyState(GLFW.GLFW_KEY_W))
        {
            thing.translateLocal(Vector3f(0.0f, 0f, -1f * td * speedPush*speed))
        }
        if (window.getKeyState(GLFW.GLFW_KEY_S))
        {
            thing.translateLocal(Vector3f(0.0f, 0f, 1f * td*speed))

        }
        if (window.getKeyState(GLFW.GLFW_KEY_A))
        {
            thing.rotateAroundPoint(0f, Math.toRadians(1f*td*speed),0f, thing.getPosition())
        }
        if (window.getKeyState(GLFW.GLFW_KEY_D)) {
            thing.rotateAroundPoint(0f, Math.toRadians(-1f*td*speed),0f,thing.getPosition())
        }
    }

    //add on zu roam

    open fun roamAltitude(thing:Transformable, td:Float, speed:Float=4f)
    {
        var a:Float =(thing.rhAlt-5f)*(Math.random()-0.5).toFloat()
        thing.translateLocal(Vector3f(0f,1f * td* a * speed,0f))
    }

}