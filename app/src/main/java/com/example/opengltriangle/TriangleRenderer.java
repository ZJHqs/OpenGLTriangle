package com.example.opengltriangle;

import static android.opengl.Matrix.orthoM;
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
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

public class TriangleRenderer implements GLSurfaceView.Renderer {

    private static final boolean DBG = false;

//    private static final String U_COLOR = "u_Color";
    private static final String U_MATRIX = "u_Matrix";
//    private static final String U_POSITION = "u_Position";
    private static final String XOFFSET = "xOffset";
    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";
    private static final int POSITION_COMPONENT_COUNT = 3;
//    private static final int COLOR_COMPONENT_COUNT = 4;

    private static final int RED = 0;
    private static final int GREEN = 1;
    private static final int BLUE = 2;
    int index = 1;
    float[] color = new float[] {1f, 0f, 0f};


    private final FloatBuffer vertexData;
//    private final FloatBuffer colorBuffer;
    private final Context context;
    private int program;
//    private int uColorLocation;
    private int aPositionLocation;
    private int aColorLocation;
    private float[] projectionMatrix = new float[16];
    private int uMatrixLocation;
//    private int uPosition;

    private float offset = 0.5f;
    private int uOffsetLocation;

    public TriangleRenderer(Context context) {
        this.context = context;

//        float[] vertices = new float[]{
//                // 第一个三角形
//                0.5f, 0.5f, 0.0f,   // 右上角
//                0.5f, -0.5f, 0.0f,  // 右下角
//                -0.5f, 0.5f, 0.0f,  // 左上角
//                // 第二个三角形
//                0.5f, -0.5f, 0.0f,  // 右下角
//                -0.5f, -0.5f, 0.0f, // 左下角
//                -0.5f, 0.5f, 0.0f   // 左上角
//        };

        float[] vertices = new float[] {
                // 位置              // 颜色
                0.5f, -0.5f, 0.0f,  1.0f, 0.0f, 0.0f,   // 右下
                -0.5f, -0.5f, 0.0f,  0.0f, 1.0f, 0.0f,   // 左下
                0.0f,  0.5f, 0.0f,  0.0f, 0.0f, 1.0f    // 顶部
        };

        vertexData = ByteBuffer.allocateDirect(vertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(vertices);

//        float[] color = new float[] {
//                1.0f, 0.0f, 0.0f, 1.0f,
//                0.0f, 1.0f, 0.0f, 1.0f,
//                0.0f, 0.0f, 1.0f, 1.0f,
//                0.0f, 1.0f, 0.0f, 1.0f,
//                1.0f, 0.0f, 0.0f, 1.0f,
//                0.0f, 0.0f, 1.0f, 1.0f
//        };
//        colorBuffer = ByteBuffer.allocateDirect(color.length * BYTES_PER_FLOAT)
//                .order(ByteOrder.nativeOrder())
//                .asFloatBuffer();
//        colorBuffer.put(color);
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

        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);

//        uPosition = GLES20.glGetUniformLocation(program, U_POSITION);
        uOffsetLocation = GLES20.glGetUniformLocation(program, XOFFSET);

//        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);

        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 6 * BYTES_PER_FLOAT, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
        vertexData.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aColorLocation, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 6 * BYTES_PER_FLOAT, vertexData);
        GLES20.glEnableVertexAttribArray(aColorLocation);
//        colorBuffer.position(0);
//        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT,
//                GLES20.GL_FLOAT, false, 0, colorBuffer);
//        GLES20.glEnableVertexAttribArray(aColorLocation);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
//        GLES20.glViewport(0, 0, width, height);
        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
        if (width > height) {
            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        }
        else {
            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

//        GLES20.glUniform4f(uPosition, 0.5f, 0.0f, 0.0f, 0.0f);
        GLES20.glUniform1f(uOffsetLocation, offset);

        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

//        GLES20.glUniform4f(uColorLocation, color[0], color[1], color[2], 1f);
//        updateColor();

//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }

    private void updateColor() {
        if (index == GREEN) {
            if (color[1] < 1f) {
                color[0] -= 0.01f;
                color[1] += 0.01f;
            }
            else {
                index = BLUE;
            }
        }
        else if (index == BLUE) {
            if (color[2] < 1f) {
                color[1] -= 0.01f;
                color[2] += 0.01f;
            }
            else {
                index = RED;
            }
        }
        else {
            if (color[0] < 1f) {
                color[2] -= 0.01f;
                color[0] += 0.01f;
            }
            else {
                index = GREEN;
            }
        }
    }
}
