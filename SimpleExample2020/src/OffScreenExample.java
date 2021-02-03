import com.jogamp.opengl.*;
import com.jogamp.opengl.util.PMVMatrix;
import java.awt.event.*;

public class OffScreenExample extends SimpleExampleBase{
  Object3D obj;
  Object3D obj2;
  OffScreen off;
  PMVMatrix mats;
  Shader shader;
  int uniformMat;
  int uniformLight;
  float t=0;

  public OffScreenExample(){
    off = new OffScreen(SCREENW, SCREENH);
    //obj = new Plane(off.getColorTexture());
    obj = new Plane(off.getDepthTexture());
    obj2 = new Plane(null);
    //obj = new BezierPatch();
    mats = new PMVMatrix();
    shader = new Shader("resource/simple.vert", "resource/simple.frag");
    addKeyListener(new OffScreenExampleKeyListener());
    addMouseMotionListener(new OffScreenExampleMouseMotionListener());
    addMouseListener(new OffScreenExampleMouseListener());
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
    off.init(gl);
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
    obj2.init(gl, mats, programName);
    gl.glUseProgram(0);
  }

  public void display(GLAutoDrawable drawable){
    final GL2GL3 gl = drawable.getGL().getGL2GL3();
    mats.glMatrixMode(GL2.GL_MODELVIEW);
    mats.glLoadIdentity();
    mats.glTranslatef(0f,0f,-2.0f);
    if(t<360){
      t = t+0.1f;
    }else{
      t = 0f;
    }
    mats.glRotatef(t,1f,0f,0f);
    mats.glMatrixMode(GL2.GL_PROJECTION);
    mats.glLoadIdentity();
    mats.glFrustumf(-1f,1f,-1f,1f,1f,100f);
    mats.update();

    gl.glUseProgram(shader.getID());
    gl.glUniformMatrix4fv(uniformMat, 4, false, mats.glGetPMvMvitMatrixf());

    off.bind(gl);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);//clear offscrn
    obj2.display(gl, mats, shader.getID());
    gl.glFlush();
    off.unbind(gl);

    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);//clear screen
    obj.display(gl, mats, shader.getID());
    gl.glUseProgram(0);
  }

  public static void main(String[] args){
    OffScreenExample t = new OffScreenExample();
    t.start();
  }

  class OffScreenExampleKeyListener implements KeyListener{
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

  class OffScreenExampleMouseMotionListener implements MouseMotionListener{
    public void mouseDragged(MouseEvent e){
      System.out.println("dragged:"+e.getX()+" "+e.getY());
    }
    public void mouseMoved(MouseEvent e){
      System.out.println("moved:"+e.getX()+" "+e.getY());
    }
  }

  class OffScreenExampleMouseListener implements MouseListener{
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
