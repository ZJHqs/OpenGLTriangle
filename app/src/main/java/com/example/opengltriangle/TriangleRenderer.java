package com.example.opengltriangle;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.opengltriangle.objects.Triangle;
import com.example.opengltriangle.programs.TriangleProgram;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleRenderer implements GLSurfaceView.Renderer {
    private static final boolean DBG = false;

    private final Context context;
    private TriangleProgram triangleProgram;
    private Triangle triangle;

    public TriangleRenderer(Context context) {
        this.context = context;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 0f);

       triangleProgram = new TriangleProgram(context);
       triangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        drawTriangles();
    }

    private void drawTriangles() {
        triangleProgram.useProgram();
        triangleProgram.setUniforms();
        triangle.bindData(triangleProgram);
        triangle.draw();
    }
}
