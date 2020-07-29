package cga.exercise.components.geometry

import org.joml.Matrix4f
import org.joml.Vector3f

open class Transformable(p:Transformable?) : ITransformable
{
    var modelMatrix : Matrix4f = Matrix4f().identity()
    var parent : Transformable? = p

    /**
     * Rotates object around its own origin.
     * @param pitch radiant angle around x-axis ccw
     * @param yaw radiant angle around y-axis ccw
     * @param roll radiant angle around z-axis ccw
     */


    override fun rotateLocal(pitch: Float, yaw: Float, roll: Float) {
        modelMatrix.rotateXYZ(pitch, yaw, roll)
    }

    fun rotateGlobal(p:Float, y:Float, r:Float)
    {
        var m : Matrix4f = Matrix4f().identity()
        m.rotateX(p)
        m.rotateY(y)
        m.rotateZ(r)
        modelMatrix = m.mul(modelMatrix)
    }

    /**
     * Rotates object around given rotation center.
     * @param pitch radiant angle around x-axis ccw
     * @param yaw radiant angle around y-axis ccw
     * @param roll radiant angle around z-axis ccw
     * @param altMidpoint rotation center
     */
    override fun rotateAroundPoint(pitch: Float, yaw: Float, roll: Float, altMidpoint: Vector3f) {
        var m : Matrix4f = Matrix4f().identity()
        m.translate(altMidpoint)
        m.rotateXYZ(pitch, yaw, roll)
        m.translate(Vector3f(altMidpoint).negate())
        modelMatrix = m.mul(modelMatrix)
    }

    /**
     * Translates object based on its own coordinate system.
     * @param deltaPos delta positions
     */
    override fun translateLocal(deltaPos: Vector3f) {
        modelMatrix.translate(deltaPos)
    }

    /**
     * Translates object based on its parent coordinate system.
     * Hint: global operations will be left-multiplied
     * @param deltaPos delta positions (x, y, z)
     */
    override fun translateGlobal(deltaPos: Vector3f) {
        var m : Matrix4f = Matrix4f().identity()
        m.translate(deltaPos)
        modelMatrix = m.mul(modelMatrix)
        /* if(parent!=null)parent!!.modelMatrix.translate(deltaPos)
        else translateLocal(deltaPos) */
    }

    /**
     * Scales object related to its own origin
     * @param scale scale factor (x, y, z)
     */
    override fun scaleLocal(scale: Vector3f) {
        modelMatrix.scale(scale)
    }

    /**
     * Returns position based on aggregated translations.
     * Hint: last column of model matrix
     * @return position
     */
    override fun getPosition(): Vector3f
    {
        return Vector3f(modelMatrix.m30(), modelMatrix.m31(), modelMatrix.m32())
    }

    /**
     * Returns position based on aggregated translations incl. parents.
     * Hint: last column of world model matrix
     * @return position
     */
    override fun getWorldPosition(): Vector3f {
        var m : Matrix4f = getWorldModelMatrix()
        return Vector3f(m.m30(), m.m31(), m.m32())

    }

    /**
     * Returns x-axis of object coordinate system
     * Hint: first normalized column of model matrix
     * @return x-axis
     */
    override fun getXAxis(): Vector3f {
        //return help(1).normalize()
        return Vector3f(modelMatrix.m00(), modelMatrix.m01(), modelMatrix.m02()).normalize()
    }

    /**
     * Returns y-axis of object coordinate system
     * Hint: second normalized column of model matrix
     * @return y-axis
     */
    override fun getYAxis(): Vector3f {
        //return help(2).normalize()
        return Vector3f(modelMatrix.m10(), modelMatrix.m11(), modelMatrix.m12()).normalize()
    }

    /**
     * Returns z-axis of object coordinate system
     * Hint: third normalized column of model matrix
     * @return z-axis
     */
    override fun getZAxis(): Vector3f {
        //return help(3).normalize()
        return Vector3f(modelMatrix.m20(), modelMatrix.m21(), modelMatrix.m22()).normalize()
    }

    /**
     * Returns x-axis of world coordinate system
     * Hint: first normalized column of world model matrix
     * @return x-axis
     */
    override fun getWorldXAxis(): Vector3f {
        /* if(parent!=null)return parent!!.help(1).normalize()
        return getXAxis() */
        var m : Matrix4f = getWorldModelMatrix()
        return Vector3f(m.m00(), m.m01(), m.m02()).normalize()
    }

    /**
     * Returns y-axis of world coordinate system
     * Hint: second normalized column of world model matrix
     * @return y-axis
     */
    override fun getWorldYAxis(): Vector3f {
        /* if(parent!=null)return parent!!.help(2).normalize()
        return getYAxis() */
        var m : Matrix4f = getWorldModelMatrix()
        return Vector3f(m.m10(), m.m11(), m.m12()).normalize()
    }

    /**
     * Returns z-axis of world coordinate system
     * Hint: third normalized column of world model matrix
     * @return z-axis
     */
    override fun getWorldZAxis(): Vector3f {
        /* if(parent!=null)return parent!!.help(3).normalize()
        return getZAxis() */
        var m : Matrix4f = getWorldModelMatrix()
        return Vector3f(m.m20(), m.m21(), m.m22()).normalize()
    }

    /**
     * Returns multiplication of world and object model matrices.
     * Multiplication has to be recursive for all parents.
     * Hint: scene graph
     * @return world modelMatrix
     */
    override fun getWorldModelMatrix(): Matrix4f {
        /* if(parent!=null)return parent!!.getWorldModelMatrix().mul(modelMatrix)
        return modelMatrix */
        if(parent!=null)return parent!!.getWorldModelMatrix().mul(modelMatrix)
        return Matrix4f(modelMatrix)
    }

    /**
     * Returns object model matrix
     * @return modelMatrix
     */
    override fun getLocalModelMatrix(): Matrix4f {
        return Matrix4f(modelMatrix)
    }
}