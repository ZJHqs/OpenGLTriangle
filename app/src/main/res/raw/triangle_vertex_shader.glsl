uniform mat4 u_Matrix;
uniform vec4 u_Position;

attribute vec3 a_Position;
attribute vec3 a_Color;

varying vec3 v_Color;

void main() {
    gl_Position = u_Matrix * (vec4(a_Position, 1.0) + u_Position);
    v_Color = a_Color;
//    v_Color = vec4(1.0, 1.0, 0.0, 1.0);
}
