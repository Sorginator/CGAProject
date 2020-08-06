package cga.exercise.components.`object`

import cga.exercise.components.camera.ProjectCamera
import cga.framework.GameWindow
import org.joml.Vector3f
import org.joml.Math
import org.joml.Matrix4f

class Ente(path: String, posX: Float, posY: Float, posZ: Float, rotX: Float= 0f, rotY: Float= 0f, rotZ: Float= 0f, scaleX: Float= 1f, scaleY: Float= 1f, scaleZ: Float= 1f, horizontalReverse: Boolean= false, verticalReverse: Boolean= false):
        Walkable(path, posX, posY, posZ, rotX, rotY, rotZ, scaleX, scaleY, scaleZ, horizontalReverse, verticalReverse) {

    var delta:Float=0f
    var dir:Boolean=true
    var temp:Float=0f

    init {
        speedForward= 90f
        speedBackwards= 90f
    }

    override fun initCamera(cam: ProjectCamera) {
        cam.rotateLocal(Math.toRadians(20f), Math.toRadians(180f), Math.toRadians(0f))
        cam.translateLocal(Vector3f(0f,50f,60f))
    }

    override fun walk(timeDifference: Float, window: GameWindow, time: Float)
    {
        //Ente normal Bewegen lassen
        super.walk(timeDifference, window, time)
        //Ente Watscheln lassen
        if (isMoving)
        {
            temp=Math.toRadians(timeDifference * 40 * speedPush)
            if (delta < 0.2f&&dir)
            {
                delta += temp
                loadedObject.rotateLocal(0f, 0f, temp)
            }
            else if(delta>-0.2f&&!dir)
            {
                delta -= temp
                loadedObject.rotateLocal(0f, 0f, temp*-1)
            }
            else
            {
                dir = !dir
            }
        }
        else
        {
            temp=Math.toRadians(timeDifference*50)
            if(delta>0.1f)
            {
                loadedObject.rotateLocal(0f,0f,temp)
                delta-=temp
            }
            else if (delta<-0.1f)
            {
                loadedObject.rotateLocal(0f,0f,temp*-1)
                delta-=temp*-1
            }
            else
            {
                loadedObject.rotateLocal(0f, 0f, delta * -1)
                delta=0f
            }
        }
    }
}