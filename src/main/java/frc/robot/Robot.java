// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  PhotonCamera m_PVCamera = new PhotonCamera("WEB_CAM");
  boolean m_HasTarget;
  PhotonPipelineResult m_Result;
  PhotonTrackedTarget m_Target;
  double m_X;

  private Joystick m_js1 = new Joystick(0);
  private DigitalOutput m_LeftLight = new DigitalOutput(1);
  private DigitalOutput m_RightLight = new DigitalOutput(1);

  // PhotonCamera camera = new PhotonCamera("WEB_CAM");
  // PhotonPipelineResult result;
  // boolean hasTarget;
  // List<PhotonTrackedTarget> targets;

  // Define target
  // PhotonTrackedTarget target;
  // Transform3d bestCameraToTarget;

  @Override
  public void robotInit() {}

  @Override
  public void robotPeriodic() {}

  @Override
  public void teleopInit() {}

  /* This function is called periodically during autonomous. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void autonomousInit() {
    m_LeftLight.set(true);
    m_RightLight.set(true);
    m_X = 0;
  }

  @Override
  public void autonomousPeriodic() {
    m_Result = m_PVCamera.getLatestResult();
    m_HasTarget = m_Result.hasTargets();

    if( m_HasTarget ){
      m_Target = m_Result.getBestTarget();
      Transform3d mCameraOutput = getCameratoTarget();
      m_X = mCameraOutput.getY();
    }

    if( m_X > 0.2 || m_X < -0.2 ){
      m_LeftLight.set(false);
      m_RightLight.set(false);
    }else{
      m_LeftLight.set(true);
      m_RightLight.set(true);
    }

    SmartDashboard.putBoolean("HasTarget", m_HasTarget);
    SmartDashboard.putNumber("X", m_X);
    SmartDashboard.putBoolean("LeftLight", !m_LeftLight.get());
    SmartDashboard.putBoolean("RightLight", !m_RightLight.get());
  }

  /**
   * Get the transform that maps camera ( X = forward, Y = left, Z = up ) to apriltag
   **/
  public Transform3d getCameratoTarget(){
    return m_HasTarget == true ? m_Target.getBestCameraToTarget() : new Transform3d( new Translation3d(0,0,0), new Rotation3d(0,0,0) );
  }
}
