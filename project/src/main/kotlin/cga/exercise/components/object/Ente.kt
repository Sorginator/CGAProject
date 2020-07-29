package cga.exercise.components.`object`

import cga.exercise.components.camera.ProjectCamera
import cga.framework.GameWindow
import org.joml.Vector3f
import org.joml.Math

class Ente(path: String, posX: Float, posY: Float, posZ: Float, rotX: Float= 0f, rotY: Float= 0f, rotZ: Float= 0f, scaleX: Float= 1f, scaleY: Float= 1f, scaleZ: Float= 1f, horizontalReverse: Boolean= false, verticalReverse: Boolean= false):
        Walkable(path, posX, posY, posZ, rotX, rotY, rotZ, scaleX, scaleY, scaleZ, horizontalReverse, verticalReverse) {

    var watschelRichtung: Int = 1
    var watschelRot: Float = 0.0f
    var rotAenderung: Float = 0.0f
    var rotZurücksetzen: Boolean = false

    init {
        speedForward= 90f
        speedBackwards= 90f
    }

    fun initCamera(cam: ProjectCamera) {
        cam.rotateLocal(Math.toRadians(-20f),0f,0f)
        cam.translateLocal(Vector3f(0f,50f,60f))
    }

    override fun walk(timeDifference: Float, window: GameWindow, time: Float) {
        //Ente normal Bewegen lassen
        super.walk(timeDifference, window, time)
        //Ente Watscheln lassen
        if (isMoving) {
            if (watschelRot < 20) {
                rotAenderung += timeDifference * watschelRichtung * 50 * speedPush
                loadedObject.rotateLocal(0f, 0f, Math.toRadians(timeDifference * watschelRichtung * 50 * speedPush))
                watschelRot += timeDifference * 50 * speedPush
            } else {
                watschelRichtung *= -1
                watschelRot = -20f
            }
            rotZurücksetzen = true
        } else {
            //Wenn die Ente nicht mehr geht, aber noch schräg steht --> grade stellen
            if (rotZurücksetzen) {
                // Wie viel Grad fehlen bis zur Ausgangsstellung
                var tempAenderung = timeDifference * 50 * speedPush
                if ((rotAenderung - tempAenderung) < 0.0f) {
                    tempAenderung = rotAenderung * -1
                }
                //Watschelrichtung umkehren
                if (watschelRichtung == 1 && watschelRot > 0) {
                    tempAenderung *= (watschelRichtung * -1)
                } else {
                    tempAenderung *= watschelRichtung
                }

                //Variablen anpassen
                if ((Math.abs(rotAenderung) - Math.abs(tempAenderung)) <= 0) {
                    watschelRot = 0.0f
                    rotAenderung = 0.0f
                    rotZurücksetzen = false
                } else {
                    watschelRot -= Math.abs(tempAenderung)
                    rotAenderung += tempAenderung
                }

                //Ausführen der Rotation
                loadedObject.rotateLocal(0f, 0f, Math.toRadians(tempAenderung))
            }
        }
    }
}