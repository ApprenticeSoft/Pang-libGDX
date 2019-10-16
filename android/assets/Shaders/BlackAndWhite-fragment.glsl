varying vec4 v_color;				//varying = partagé par le vertex shader et le fragment shader
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;		//sampler2D = prend la couleur d'un pixel d'une texture
uniform mat4 u_projTrans;

void main(){
	//gl_FragColor = texture2D(u_sampler2D, v_texCoord0) * v_color;  //Classic passthrough shader
	
	
        vec3 color = texture2D(u_sampler2D, v_texCoord0).rgb;
        float gray = (color.r + color.g + color.b) / 3.0;
        vec3 grayscale = vec3(gray);

        gl_FragColor = vec4(grayscale, 1.0);
}