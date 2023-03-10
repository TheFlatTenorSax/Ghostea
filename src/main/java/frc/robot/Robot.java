// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  private TalonFX right1;
  private TalonFX right2;
  private TalonFX left1;
  private TalonFX left2;
  private CANSparkMax shootyThing;
  private CANSparkMax intake;
  private CANSparkMax neo2;
  private DigitalInput beep;
  private XboxController gamer;
  private double lMotor;
  private double rMotor;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    // m_robotContainer = new RobotContainer(); <-- stupid line. makes robot funny. (temporarily)
    right1 = new TalonFX(4);
    right2 = new TalonFX(2);
    left1 = new TalonFX(3);
    left2 = new TalonFX(1); 
    shootyThing = new CANSparkMax(1, MotorType.kBrushless);
    intake = new CANSparkMax(3, MotorType.kBrushless);
    neo2 = new CANSparkMax(2,MotorType.kBrushless);
    beep = new DigitalInput(9);
    gamer = new XboxController(0);
    left2.follow(left1);
    right2.follow(right1);
    left1.setInverted(true);
    left2.setInverted(true);
    right1.setNeutralMode(NeutralMode.Brake);
    right2.setNeutralMode(NeutralMode.Brake);
    left1.setNeutralMode(NeutralMode.Brake);
    left2.setNeutralMode(NeutralMode.Brake);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.

    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    double leftSide = gamer.getLeftY(); //Left Stick up/down will control left set of wheels
    double rightSide = gamer.getRightX(); //Right Stick up/down will control right set of wheels (getRightY = get input from Y-axis)
    double left = leftSide + rightSide;
    double right = leftSide - rightSide;
    boolean intakePressed = gamer.getRightBumper(); // Right bumper on controller activates intake (hopefully)
    boolean pressed = gamer.getLeftBumper();
    boolean moveitmoveit = gamer.getAButtonPressed();
    boolean bop = beep.get(); // Sensor stuff
    SmartDashboard.putBoolean("Sensor", bop);
    SmartDashboard.putNumber("LeftAutoMotors", lMotor);
    SmartDashboard.putNumber("RightAutoMotors", rMotor);
  

    /*Order of events:
     *get input from gamer
     * translate beep bop to vroom vroom
     * tell speed controller how much vroom vroom
     */

    //ayushi is bored
    //ayushi is tired
    //ayushi went to sleep
    //goodnight.
    //Copyright(r) Ryan Chan 2023. All rights reserved.

    // Arcade drive mathy stuff
    if(left > 1.0){
      left = 1.0;
    }else if(right > 1.0){
      right = 1.0;
    }else if(left < -1.0){
      left = -1.0;
    }else if(right < -1.0){
      right = -1.0;
    }

    right1.set(ControlMode.PercentOutput, right);
    left1.set(ControlMode.PercentOutput, left);

    if(pressed == false || !bop){ // If the left bumper on the controller is not pressed (|| = or) if bop senses something...
      shootyThing.set(0);
    }else if(pressed == true){ //If the left bumper IS pressed...
      shootyThing.set(-0.6);
    }

    if(intakePressed == false){
      intake.set(0);
      neo2.set(0);
    }else if(intakePressed == true){
      intake.set(-0.3);
      neo2.set(-0.3);
    } 

    if(moveitmoveit){ //Does not work (Why...?)
      lMotor = left1.getSelectedSensorPosition();
      rMotor = right1.getSelectedSensorPosition();
      double new_pos_left = lMotor + 2048.0*1.910*12.75;
      double new_pos_right = rMotor + 2048.0*1.910*12.75;
      SmartDashboard.putNumber("LeftAutoStarting", lMotor);
    SmartDashboard.putNumber("RightAutoStarting", rMotor);
    SmartDashboard.putNumber("LeftAutoMotors", new_pos_left);
    SmartDashboard.putNumber("RightAutoMotors", new_pos_right);
      left1.set(ControlMode.Position, new_pos_left);
      right1.set(ControlMode.Position, new_pos_right);
    }

  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
