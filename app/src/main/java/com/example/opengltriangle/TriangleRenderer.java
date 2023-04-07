package com.example.opengltriangle;

import static com.example.opengltriangle.Constants.BYTES_PER_FLOAT;
import static com.example.opengltriangle.Constants.BYTES_PER_SHORT;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.opengltriangle.utils.ShaderHelper;
import com.example.opengltriangle.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleRenderer implements GLSurfaceView.Renderer {

    private static final boolean DBG = false;

    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private static final int POSITION_COMPONENT_COUNT = 3;



    private FloatBuffer vertexData;
    private ShortBuffer indexBuffer;
    private final Context context;
    private int program;
    private int uColorLocation;
    private int aPositionLocation;

    float[] vertices = new float[] {
            0.5f,  0.5f, 0.0f,  // top right
            0.5f, -0.5f, 0.0f,  // bottom right
            -0.5f, -0.5f, 0.0f,  // bottom left
            -0.5f,  0.5f, 0.0f   // top left
    };

    short[] indices = new short[] {
            0, 1, 3,
            1, 2, 3
    };





    public TriangleRenderer(Context context) {
        this.context = context;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 0f);

        String vertexShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.triangle_vertex_shader);
        String fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.triangle_fragment_shader);

        program = ShaderHelper.buildProgram(vertexShaderSource, fragmentShaderSource);

        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);

        vertexData = ByteBuffer.allocateDirect(vertices.length * BYTES_PER_FLOAT).
                order(ByteOrder.nativeOrder()).asFloatBuffer();
        // TODO::这里是否应该直接把put放进去？
        vertexData.put(vertices);


        indexBuffer = ByteBuffer.allocateDirect(6 * BYTES_PER_SHORT).
                order(ByteOrder.nativeOrder()).asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(program);
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);

        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
    }
}
