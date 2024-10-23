// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.simulation.AddressableLEDSim;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
  private AddressableLED m_led;
  private AddressableLEDBuffer m_ledBuffer;
  private AddressableLEDSim m_ledSim;

  // Store what the last hue of the first pixel is
  private int m_rainbowFirstPixelHue;

  private int red;
  private int green;
  private int blue;
  private SendableChooser<String> m_ledChooser;
  private Double[] rgb = {255.0, 255.0, 255.0};
  private String ledMode;

  @Override
  public void robotInit() {
    m_ledChooser = new SendableChooser<String>();
    m_ledChooser.setDefaultOption("Custom RGB", "Custom");
    m_ledChooser.addOption("Periodic Method", "Periodic");
    m_ledChooser.addOption("Rainbow", "Rainbow");
    m_ledChooser.addOption("Red", "Red");
    m_ledChooser.addOption("White", "White");
    m_ledChooser.addOption("Blue", "Blue");
    m_ledChooser.addOption("Pink", "Pink");
    m_ledChooser.addOption("Purple", "Purple");
    m_ledChooser.addOption("Green", "Green");
    m_ledChooser.addOption("Orange", "Orange");
    m_ledChooser.addOption("Yellow", "Yellow");
    m_ledChooser.addOption("LEDs Off", "Nothing");
    SmartDashboard.putData("LED Picker", m_ledChooser);
    SmartDashboard.putNumberArray("Custom LED RGB Value", rgb);

    // PWM port 0
    // Must be a PWM header, not MXP or DIO
    m_led = new AddressableLED(1);

    // Reuse buffer
    // Default to a length of 60, start empty output
    // Length is expensive to set, so only set it once, then just update data
    m_ledBuffer = new AddressableLEDBuffer(106);
    m_led.setLength(m_ledBuffer.getLength());

    // Set the data
    m_led.setData(m_ledBuffer);
    m_led.start();

    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      // Sets the specified LED to the RGB values for white
      m_ledBuffer.setRGB(i, 255, 255, 255);
    }

    m_ledSim = new AddressableLEDSim(m_led);
  }

  @Override
  public void robotPeriodic() {
    ledMode = m_ledChooser.getSelected();
    switch (ledMode) {
      case "Rainbow": rainbow();
        break;
      case "Red": red();
        break;
      case "White": white();
        break;
      case "Blue": blue();
        break;
      case "Pink": pink();
        break;
      case "Purple": purple();
        break;
      case "Green": green();
        break;
      case "Orange": orange();
        break;
      case "Yellow": yellow();
        break;
      case "Nothing": nothing();
        break;
      case "Custom": custom();
        break;
    }
    // Set the LEDs
    m_led.setData(m_ledBuffer);
  }

  @Override
  public void disabledInit() {
    red();
  }

  @Override
  public void autonomousInit() {
    white();
  }

  @Override
  public void teleopInit() {
    pink();
  }

  @Override
  public void testInit() {
    blue();
  }

  private void rainbow() {
    // For every pixel
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      // Calculate the hue - hue is easier for rainbows because the color
      // shape is a circle so only one value needs to precess
      final var hue = (m_rainbowFirstPixelHue + (i * 180 / m_ledBuffer.getLength())) % 180;
      // Set the value
      m_ledBuffer.setHSV(i, hue, 255, 128);
    }
    // Increase by to make the rainbow "move"
    m_rainbowFirstPixelHue += 3;
    // Check bounds
    m_rainbowFirstPixelHue %= 180;
  }

  private void red() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, 255, 0, 0);
    }
  }

  private void white() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, 255, 255, 255);
    }
  }

  private void blue() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, 0, 0, 255);
    }
  }

  private void pink() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, 255, 92, 186);
    }
  }

  private void purple() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, 255, 0, 255);
    }
  }

  private void green() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, 0, 255, 0);
    }
  }

  private void orange() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, 255, 145, 40);
    }
  }

  private void yellow() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, 255, 255, 0);
    }
  }

  private void custom() {
    rgb = SmartDashboard.getNumberArray("Custom LED RGB Value", new Double[]{0.0, 0.0, 0.0});
    red = rgb[0].intValue();
    green = rgb[1].intValue();
    blue = rgb[2].intValue();

    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, red, green, blue);
    }
  }

  private void nothing() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, 0, 0, 0);
    }
  }
}
