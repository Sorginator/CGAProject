package cga.exercise.components.camera

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f
import org.joml.Math

class ProjectCamera(p: Transformable?,
                 Field_of_view : Float = Math.toRadians(90f),
                 aspect_ratio : Float = 16.0f/9.0f,
                 Near_Plane : Float = 0.1f,
                 Far_Plane : Float = 100.0f) : Transformable(p), ICamera
{

    var FieldOfView : Float = Field_of_view
    var Seitenverhältnis : Float = aspect_ratio
    var NearPlane : Float = Near_Plane
    var FarPlane : Float = Far_Plane

    override fun getCalculateViewMatrix(): Matrix4f {
        return Matrix4f().lookAt(getWorldPosition(), getWorldPosition().sub(getWorldZAxis()), getWorldYAxis())
    }

    override fun getCalculateProjectionMatrix(): Matrix4f {
        return Matrix4f().perspective(FieldOfView, Seitenverhältnis, NearPlane, FarPlane)
    }

    override fun bind(shader: ShaderProgram) {
        shader.setUniform("view_matrix", getCalculateViewMatrix(), false)
        shader.setUniform("projection_matrix", getCalculateProjectionMatrix(), false)
    }
}