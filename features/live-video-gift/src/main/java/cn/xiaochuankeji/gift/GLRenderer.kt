package cn.xiaochuankeji.gift

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


abstract class GLRenderer() {
    protected var curRotation: Int
    protected var renderVertices: FloatBuffer? = null
    protected var textureVertices: Array<FloatBuffer?>
    protected var programHandle: Int = 0
    private var vertexShaderHandle: Int = 0
    private var fragmentShaderHandle: Int = 0
    protected var textureHandle: Int = 0
    protected var positionHandle: Int = 0
    protected var texCoordHandle: Int = 0
    protected var texture_in: Int
    private var width: Int = 0
    private var height: Int = 0
    private var customSizeSet: Boolean
    private var initialized: Boolean = false
    private var sizeChanged: Boolean
    /**
     * Returns the red component of the background colour currently set for this GLRenderer.
     * @return red
     * The red component of the background colour.
     */
    /**
     * Sets only the red component of the background colour currently set for this GLRenderer.
     * @param red
     * The red component to set as the background colour.
     */
    var backgroundRed: Float = 0f
    /**
     * Returns the green component of the background colour currently set for this GLRenderer.
     * @return green
     * The green component of the background colour.
     */
    /**
     * Sets only the green component of the background colour currently set for this GLRenderer.
     * @param green
     * The green component to set as the background colour.
     */
    var backgroundGreen: Float = 0f
    /**
     * Returns the blue component of the background colour currently set for this GLRenderer.
     * @return blue
     * The blue component of the background colour.
     */
    /**
     * Sets only the blue component of the background colour currently set for this GLRenderer.
     * @param blue
     * The blue component to set as the background colour.
     */
    var backgroundBlue: Float = 0f
    /**
     * Returns the alpha component of the background colour currently set for this GLRenderer.
     * @return alpha
     * The alpha component of the background colour.
     */
    /**
     * Sets only the alpha component of the background colour currently set for this GLRenderer.
     * @param alpha
     * The alpha component to set as the background colour.
     */
    var backgroundAlpha: Float = 0f

    protected fun setRenderVertices(vertices: FloatArray) {
        renderVertices = ByteBuffer.allocateDirect(vertices.size * 4).order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        renderVertices!!.put(vertices)!!.position(0)
    }

    /**
     * Returns the current width the GLRenderer is rendering at.
     * @return width
     */
    fun getWidth(): Int {
        return width
    }

    /**
     * Returns the current height the GLRenderer is rendering at.
     * @return height
     */
    fun getHeight(): Int {
        return height
    }

    protected fun setWidth(width: Int) {
        if (!customSizeSet && this.width != width) {
            this.width = width
            sizeChanged = true
        }
    }

    protected fun setHeight(height: Int) {
        if (!customSizeSet && this.height != height) {
            this.height = height
            sizeChanged = true
        }
    }

    /**
     * Rotates the renderer clockwise by 90 degrees a given number of times.
     * @param numOfTimes
     * The number of times this renderer should be rotated clockwise by 90 degrees.
     */
    fun rotateClockwise90Degrees(numOfTimes: Int) {
        curRotation += numOfTimes
        curRotation = curRotation % 4
        if (numOfTimes % 2 == 1) {
            val temp: Int = width
            width = height
            height = temp
        }
    }

    /**
     * Rotates the renderer counter-clockwise by 90 degrees a given number of times.
     * @param numOfTimes
     * The number of times this renderer should be rotated counter-clockwise by 90 degrees.
     */
    fun rotateCounterClockwise90Degrees(numOfTimes: Int) {
        curRotation += 4 - (numOfTimes % 4)
        curRotation = curRotation % 4
        if (numOfTimes % 2 == 1) {
            val temp: Int = width
            width = height
            height = temp
        }
    }

    /**
     * Sets the render size of the renderer to the given width and height.
     * This also prevents the size of the renderer from changing automatically
     * when one of the source(s) of the renderer has a size change.  If the renderer
     * has been rotated an odd number of times, the width and height will be swapped.
     * @param width
     * The width at which the renderer should draw at.
     * @param height
     * The height at which the renderer should draw at.
     */
    fun setRenderSize(width: Int, height: Int) {
        customSizeSet = true
        if (curRotation % 2 == 1) {
            this.width = height
            this.height = width
        } else {
            this.width = width
            this.height = height
        }
        sizeChanged = true
    }

    open protected fun passShaderValues() {
        renderVertices!!.position(0)
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 8, renderVertices)
        GLES20.glEnableVertexAttribArray(positionHandle)
        textureVertices[curRotation]!!.position(0)
        GLES20.glVertexAttribPointer(
            texCoordHandle,
            2,
            GLES20.GL_FLOAT,
            false,
            8,
            textureVertices[curRotation]
        )
        GLES20.glEnableVertexAttribArray(texCoordHandle)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture_in)
        GLES20.glUniform1i(textureHandle, 0)
    }

    protected fun bindShaderAttributes() {
        GLES20.glBindAttribLocation(
            programHandle, 0,
            ATTRIBUTE_POSITION
        )
        GLES20.glBindAttribLocation(
            programHandle, 1,
            ATTRIBUTE_TEXCOORD
        )
    }

    open protected fun initShaderHandles() {
        textureHandle =
            GLES20.glGetUniformLocation(
                programHandle,
                UNIFORM_TEXTURE0
            )
        positionHandle =
            GLES20.glGetAttribLocation(
                programHandle,
                ATTRIBUTE_POSITION
            )
        texCoordHandle =
            GLES20.glGetAttribLocation(
                programHandle,
                ATTRIBUTE_TEXCOORD
            )
    }

    /**
     * Re-initializes the filter on the next drawing pass.
     */
    fun reInitialize() {
        initialized = false
    }

    /**
     * Draws the given texture using OpenGL and the given vertex and fragment shaders.
     * Calling of this method is handled by the [FastImageProcessingPipeline] or other filters
     * and should not be called manually.
     */
    open fun onDrawFrame() {
        if (!initialized) {
            initWithGLContext()
            initialized = true
        }
        if (sizeChanged) {
            handleSizeChange()
            sizeChanged = false
        }
        drawFrame()
    }

    /**
     * Cleans up the opengl objects for this renderer.  Must be called with opengl context.
     * Normally called by [FastImageProcessingPipeline].
     */
    open fun destroy() {
        initialized = false
        if (programHandle != 0) {
            GLES20.glDeleteProgram(programHandle)
            programHandle = 0
        }
        if (vertexShaderHandle != 0) {
            GLES20.glDeleteShader(vertexShaderHandle)
            vertexShaderHandle = 0
        }
        if (fragmentShaderHandle != 0) {
            GLES20.glDeleteShader(fragmentShaderHandle)
            fragmentShaderHandle = 0
        }
    }

    open protected fun initWithGLContext() {
        val vertexShader: String = getVertexShader()
        val fragmentShader: String = getFragmentShader()
        vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
        var errorInfo: String = "none"
        if (vertexShaderHandle != 0) {
            GLES20.glShaderSource(vertexShaderHandle, vertexShader)
            GLES20.glCompileShader(vertexShaderHandle)
            val compileStatus: IntArray = IntArray(1)
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
            if (compileStatus[0] == 0) {
                errorInfo = GLES20.glGetShaderInfoLog(vertexShaderHandle)
                GLES20.glDeleteShader(vertexShaderHandle)
                vertexShaderHandle = 0
            }
        }
        if (vertexShaderHandle == 0) {
            throw RuntimeException("$this: Could not create vertex shader. Reason: $errorInfo")
        }
        fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)
        if (fragmentShaderHandle != 0) {
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader)
            GLES20.glCompileShader(fragmentShaderHandle)
            val compileStatus: IntArray = IntArray(1)
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
            if (compileStatus.get(0) == 0) {
                errorInfo = GLES20.glGetShaderInfoLog(fragmentShaderHandle)
                GLES20.glDeleteShader(fragmentShaderHandle)
                fragmentShaderHandle = 0
            }
        }
        if (fragmentShaderHandle == 0) {
            throw RuntimeException("$this: Could not create fragment shader. Reason: $errorInfo")
        }
        programHandle = GLES20.glCreateProgram()
        if (programHandle != 0) {
            GLES20.glAttachShader(programHandle, vertexShaderHandle)
            GLES20.glAttachShader(programHandle, fragmentShaderHandle)
            bindShaderAttributes()
            GLES20.glLinkProgram(programHandle)
            val linkStatus: IntArray = IntArray(1)
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus.get(0) == 0) {
                GLES20.glDeleteProgram(programHandle)
                programHandle = 0
            }
        }
        if (programHandle == 0) {
            throw RuntimeException("Could not create program.")
        }
        initShaderHandles()
    }

    open protected fun handleSizeChange() {}
    open protected fun drawFrame() {
        if (texture_in == 0) {
            return
        }
        GLES20.glViewport(0, 0, width, height)
        GLES20.glUseProgram(programHandle)
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glClearColor(
            backgroundRed,
            backgroundGreen,
            backgroundBlue,
            backgroundAlpha
        )
        passShaderValues()
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    open fun getVertexShader(): String {
        return ("attribute vec4 " + ATTRIBUTE_POSITION + ";\n"
                + "attribute vec2 " + ATTRIBUTE_TEXCOORD + ";\n"
                + "varying vec2 " + VARYING_TEXCOORD + ";\n"
                + "void main() {\n"
                + "  " + VARYING_TEXCOORD + " = " + ATTRIBUTE_TEXCOORD + ";\n"
                + "   gl_Position = " + ATTRIBUTE_POSITION + ";\n"
                + "}\n")
    }

    open fun getFragmentShader(): String {
        return ("precision mediump float;\n"
                + "uniform sampler2D " + UNIFORM_TEXTURE0 + ";\n"
                + "varying vec2 " + VARYING_TEXCOORD + ";\n"
                + "void main(){\n"
                + "   gl_FragColor = texture2D(" + UNIFORM_TEXTURE0 + "," + VARYING_TEXCOORD + ");\n"
                + "}\n")
    }

    /**
     * Sets the background colour for this GLRenderer to the given colour in rgba space.
     * @param red
     * The red component of the colour.
     * @param green
     * The green component of the colour.
     * @param blue
     * The blue component of the colour.
     * @param alpha
     * The alpha component of the colour.
     */
    fun setBackgroundColour(
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        backgroundRed = red
        backgroundGreen = green
        backgroundBlue = blue
        backgroundAlpha = alpha
    }

    companion object {
        val ATTRIBUTE_POSITION: String = "a_Position"
        val ATTRIBUTE_TEXCOORD: String = "a_TexCoord"
        val VARYING_TEXCOORD: String = "v_TexCoord"
        protected val UNIFORM_TEXTUREBASE: String = "u_Texture"
        val UNIFORM_TEXTURE0: String = UNIFORM_TEXTUREBASE + 0
    }

    init {
        setRenderVertices(
            floatArrayOf(
                -1f, -1f,
                1f, -1f,
                -1f, 1f,
                1f, 1f
            )
        )
        textureVertices = arrayOfNulls<FloatBuffer>(4)
        val texData0: FloatArray = floatArrayOf(
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
        )
        textureVertices[0] =
            ByteBuffer.allocateDirect(texData0.size * 4).order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        textureVertices[0]!!.put(texData0)!!.position(0)
        val texData1: FloatArray = floatArrayOf(
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
        )
        textureVertices[1] =
            ByteBuffer.allocateDirect(texData1.size * 4).order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        textureVertices[1]!!.put(texData1)!!.position(0)
        val texData2: FloatArray = floatArrayOf(
            1.0f, 1.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f
        )
        textureVertices[2] =
            ByteBuffer.allocateDirect(texData2.size * 4).order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        textureVertices[2]!!.put(texData2)!!.position(0)
        val texData3: FloatArray = floatArrayOf(
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 1.0f
        )
        textureVertices[3] =
            ByteBuffer.allocateDirect(texData3.size * 4).order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        textureVertices[3]!!.put(texData3)!!.position(0)
        curRotation = 0
        texture_in = 0
        customSizeSet = false
        initialized = false
        sizeChanged = false
    }
}
