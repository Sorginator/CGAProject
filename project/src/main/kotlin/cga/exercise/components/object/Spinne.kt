package cga.exercise.components.`object`

import cga.exercise.components.camera.ProjectCamera
import org.joml.Math
import org.joml.Vector3f

class Spinne(posX: Float, posY: Float, posZ: Float, rotX: Float= 0f, rotY: Float= 0f, rotZ: Float= 0f, scaleX: Float= 0.0007f, scaleY: Float= 0.0007f, scaleZ: Float= 0.0007f):
        Walkable("assets/complex objects/Spinne/Only_Spider_with_Animations_Export.obj", posX, posY, posZ, rotX, rotY, rotZ, scaleX, scaleY, scaleZ, false, false) {

    init {

    }

    fun initCamera(cam: ProjectCamera) {
        cam.rotateLocal(Math.toRadians(-20f),0f,0f)
        cam.translateLocal(Vector3f(0f,50f,60f))
    }
}