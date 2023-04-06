precision mediump float;

//uniform vec4 u_Color;
varying vec3 v_Color;

void main() {
    gl_FragColor = vec4(v_Color, 1.0);
//    gl_FragColor = u_Color;
}
