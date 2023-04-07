package com.example.opengltriangle.programs;

import static android.opengl.GLES20.glUseProgram;

import android.content.Context;
import android.opengl.GLES20;

import com.example.opengltriangle.R;
import com.example.opengltriangle.utils.ShaderHelper;
import com.example.opengltriangle.utils.TextResourceReader;

public class TriangleProgram {

    private static final String U_COLOR = "u_Color";

    private static final String A_POSITION = "a_Position";

    private final int uColorLocation;
    private final int aPositionLocation;

    private final int program;

    public TriangleProgram(Context context) {
        program = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(
                        context, R.raw.triangle_vertex_shader),
                TextResourceReader.readTextFileFromResource(
                        context, R.raw.triangle_fragment_shader));

        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public void useProgram() {
        // Set the current OpenGL shader program to this program.
        glUseProgram(program);
    }

    public void setUniforms() {
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
    }
}
