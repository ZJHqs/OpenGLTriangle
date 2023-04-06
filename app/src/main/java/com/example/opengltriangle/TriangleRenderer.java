package com.example.opengltriangle;

import static com.example.opengltriangle.Constants.BYTES_PER_FLOAT;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.opengltriangle.utils.ShaderHelper;
import com.example.opengltriangle.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleRenderer implements GLSurfaceView.Renderer {

    private static final boolean DBG = false;

    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private static final int POSITION_COMPONENT_COUNT = 3;



    private final FloatBuffer vertexData;
    private final Context context;
    private int program;
    private int uColorLocation;
    private int aPositionLocation;






    public TriangleRenderer(Context context) {
        this.context = context;

        float[] vertices = new float[]{
                // 第一个三角形
                0.5f, 0.5f, 0.0f,   // 右上角
                0.5f, -0.5f, 0.0f,  // 右下角
                -0.5f, 0.5f, 0.0f,  // 左上角
                // 第二个三角形
                0.5f, -0.5f, 0.0f,  // 右下角
                -0.5f, -0.5f, 0.0f, // 左下角
                -0.5f, 0.5f, 0.0f   // 左上角
        };

        vertexData = ByteBuffer.allocateDirect(vertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(vertices);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 0f);

        String vertexShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.triangle_vertex_shader);
        String fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.triangle_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        if (DBG) {
            ShaderHelper.validateProgram(program);
        }

        GLES20.glUseProgram(program);
        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);

        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);

        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUniform4f(uColorLocation, 1.0f, 0f, 0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }
}
