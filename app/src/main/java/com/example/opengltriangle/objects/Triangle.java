package com.example.opengltriangle.objects;

import android.opengl.GLES20;

import com.example.opengltriangle.programs.TriangleProgram;
import com.example.opengltriangle.utils.VertexArray;

import java.nio.ByteBuffer;

public class Triangle {
   private static final int POSITION_COMPONENT_COUNT = 3;
   private VertexArray vertexArray;
   private final ByteBuffer indexArray;

   public Triangle() {
       vertexArray = new VertexArray(new float[] {
               0.5f, 0.5f, 0.0f,   // 右上角
               0.5f, -0.5f, 0.0f,  // 右下角
               -0.5f, -0.5f, 0.0f, // 左下角
               -0.5f, 0.5f, 0.0f   // 左上角
       });

//       vertexArray = new VertexArray(new float[] {
//               // 第一个三角形
//               0.5f, 0.5f, 0.0f,   // 右上角
//               0.5f, -0.5f, 0.0f,  // 右下角
//               -0.5f, 0.5f, 0.0f,  // 左上角
//               // 第二个三角形
//               0.5f, -0.5f, 0.0f,  // 右下角
//               -0.5f, -0.5f, 0.0f, // 左下角
//               -0.5f, 0.5f, 0.0f   // 左上角
//       });

       indexArray = ByteBuffer.allocateDirect(3 * 2).
               put(new byte[] {
                       0, 1, 3, // 第一个三角形
                       1, 2, 3  // 第二个三角形
               });
       indexArray.position(0);
   }

   public void bindData(TriangleProgram triangleProgram) {
       vertexArray.setVertexAttribPointer(0,
               triangleProgram.getPositionAttributeLocation(),
               POSITION_COMPONENT_COUNT, 0);
   }

   public void draw() {
       GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_BYTE, indexArray);
//       GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
   }
}
