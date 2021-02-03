import com.jogamp.opengl.*;
import com.jogamp.opengl.util.PMVMatrix;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.KeyEvent;

public class SimpleRotation implements GLEventListener{
  final Object3D obj;
  final PMVMatrix mats;
  final Shader shader;
  int uniformMat;
  int uniformLight;
  float t=0;
  float t1=0;
  float t2=0;
  float r1=0;
  float r2=0;
  static final int SCREENH=320;
  static final int SCREENW=320;

  public SimpleRotation(){
    obj = new Cube();
    mats = new PMVMatrix();
    shader = new Shader("resource/simple.vert", "resource/simple.frag");
  }

  public void init(GLAutoDrawable drawable){
    final GL2GL3 gl = drawable.getGL().getGL2GL3();
    if(gl.isGL4()){
      drawable.setGL(new DebugGL4(drawable.getGL().getGL4()));
    }else if(gl.isGL3()){
      drawable.setGL(new DebugGL3(drawable.getGL().getGL3()));
    }else if(gl.isGL2()){
      drawable.setGL(new DebugGL2(drawable.getGL().getGL2()));
    }
    //drawable.getGL().getGL2();
    gl.glViewport(0, 0, SCREENW, SCREENH);

    // Clear color buffer with black
    gl.glClearColor(1.0f, 0.5f, 1.0f, 1.0f);
    gl.glClearDepth(1.0f);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    gl.glEnable(GL2.GL_DEPTH_TEST);
    gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT,1);
    gl.glFrontFace(GL.GL_CCW);
    gl.glEnable(GL.GL_CULL_FACE);
    gl.glCullFace(GL.GL_BACK);

    gl.glCreateShader(GL2GL3.GL_VERTEX_SHADER);
    shader.init(gl);
    int programName =shader.getID();
    gl.glBindAttribLocation(programName,Object3D.VERTEXPOSITION, "inposition");
    gl.glBindAttribLocation(programName,Object3D.VERTEXCOLOR, "incolor");
    gl.glBindAttribLocation(programName,Object3D.VERTEXNORMAL, "innormal");
    gl.glBindAttribLocation(programName,Object3D.VERTEXTEXCOORD0,"intexcoord0");
    shader.link(gl);
    uniformMat = gl.glGetUniformLocation(programName, "mat");
    uniformLight = gl.glGetUniformLocation(programName, "lightdir");
    gl.glUseProgram(programName);
    gl.glUniform3f(uniformLight, 0f, 10f, -10f);
    obj.init(gl, mats, programName);
    gl.glUseProgram(0);
  }

  public void display(GLAutoDrawable drawable){
    final GL2GL3 gl = drawable.getGL().getGL2GL3();
    gl.glUseProgram(shader.getID());
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    mats.glMatrixMode(GL2.GL_PROJECTION);
    mats.glLoadIdentity();
    mats.glFrustumf(-0.5f,0.5f,-0.5f,0.5f,1f,100f);

    mats.glMatrixMode(GL2.GL_MODELVIEW);
    mats.glLoadIdentity(); /* set 4x4 identity matrix, I*/
    mats.glTranslatef(0f,0f,-18.0f); /* multiply translation matrix, I*T */
    if(t<360){
      t = t+0.3f;
    }else{
      t = 0f;
    }
    if(t1<360){
      t1 = t1+0.3f*365;
    }else{
      t1 = t1+0.3f*365-360f;
    }
    if(t2<360){
      t2 = t2+0.3f*12;
    }else{
      t2 = 0f;
    }

    mats.glRotatef(30f,1f,1f,0f);
    mats.update();
    gl.glUniformMatrix4fv(uniformMat, 4, false, mats.glGetPMvMvitMatrixf());

    obj.display(gl, mats, shader.getID()); /* I*T*R*obj */

    mats.glRotatef(t,1f,1f,0f); /* multiply rotation matrix, I*T*R */
    r1 = 5f*(1f-0.6f*0.6f)/(1f-0.6f*(float)Math.cos(t*3.14f/180f));
    mats.glTranslatef(r1/1.41f,-r1/1.41f,0f); /* multiply translation matrix, I*T*R*T */
    mats.glScalef(0.3f, 0.3f, 0.3f);/* multiply scaling matrix, I*T*R*T*S */
    mats.glRotatef(t1,1f,0.4f,0f); /* multiply rotation matrix, I*T*R*T*S*R */
    mats.update();
    gl.glUniformMatrix4fv(uniformMat, 4, false, mats.glGetPMvMvitMatrixf());

    obj.display(gl, mats, shader.getID()); /* I*T*R*T*S*R*obj */

    mats.glRotatef(-t1,1f,0.4f,0f); /* cancel rotation of the earth */
    mats.glRotatef(t2,1f,0.8f,0f);
    r2 = 2.5f*(1f-0.6f*0.6f)/(1f-0.6f*(float)Math.cos(t2*3.14f/180f));
    mats.glTranslatef(r2/1.41f,-r2/1.41f,0f); /* multiply translation matrix, I*T*R*T*S*R*T */
    mats.glScalef(0.2f, 0.2f, 0.2f);/* multiply scaling matrix, I*T*R*T*S*R*T*S */
    mats.update();
    gl.glUniformMatrix4fv(uniformMat, 4, false, mats.glGetPMvMvitMatrixf());

    obj.display(gl, mats, shader.getID());  /* I*T*R*T*S*R*T*S*obj */

    gl.glUseProgram(0);
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h){
  }

  public void dispose(GLAutoDrawable drawable){
  }

  public KeyListener createKeyListener(){
    return new simpleExampleKeyListener();
  }

  public MouseListener createMouseListener(){
    return new simpleExampleMouseListener();
  }
  
  public static void main(String[] args){
    SimpleRotation t = new SimpleRotation();
    new SimpleExampleBase("SimpleRotation", t, SCREENW, SCREENH).
      addKeyListener(t.createKeyListener()).
      addMouseListener(t.createMouseListener()).
      start();
  }

  class simpleExampleKeyListener implements KeyListener{
    public void keyPressed(KeyEvent e){
      int keycode = e.getKeyCode();
      System.out.print(keycode);
      if(java.awt.event.KeyEvent.VK_LEFT == keycode){
        System.out.print("a");
      }
    }
    public void keyReleased(KeyEvent e){
    }
    public void keyTyped(KeyEvent e){
    }
  }

  class simpleExampleMouseListener implements MouseListener{
    public void mouseDragged(MouseEvent e){
      System.out.println("dragged:"+e.getX()+" "+e.getY());
    }
    public void mouseMoved(MouseEvent e){
      System.out.println("moved:"+e.getX()+" "+e.getY());
    }
    public void mouseWheelMoved(MouseEvent e){
    }
    public void mouseClicked(MouseEvent e){
    }
    public void mouseEntered(MouseEvent e){
    }
    public void mouseExited(MouseEvent e){
    }
    public void mousePressed(MouseEvent e){
      System.out.println("pressed:"+e.getX()+" "+e.getY());
    }
    public void mouseReleased(MouseEvent e){
    }
  }
}
