#ifdef GL_ES
	#define PRECISION mediump
	precision PRECISION float;
#else
	#define PRECISION
#endif

uniform sampler2D u_texture;
uniform sampler2D u_mask;

varying vec4 v_color;
varying vec2 v_texCoord0;

void main()
{
    vec2 mask_coord = v_texCoord0;
    
	// clamp the texture to make it repeat
	// indefinitely
	mask_coord = fract(mask_coord);
	
	// apply the displacement
	// to the mask texture
	vec4 mask = texture2D(u_mask, mask_coord);
	
	//Création de la texture finale
    vec4 texColor = texture2D(u_texture, v_texCoord0);
    texColor.a *= mask.a;
    
    gl_FragColor = v_color * texColor;
}