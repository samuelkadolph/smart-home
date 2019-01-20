/*
 *  HomeSeer HS-WD200+
 *    SmartThings Device Handler for the HomeSeer WS200+ wall dimmer with RGB indictators.
 *
 *    Website: https://shop.homeseer.com/products/hswd200
 *
 *  Buttons
 *    Number  Description
 *    1       Tap Up
 *    2       Tap Down
 *    3       Double Tap Up
 *    4       Double Tap Down
 *    5       Triple Tap Up
 *    6       Triple Tap Down
 *    7       Quadruple Tap Up
 *    8       Quadruple Tap Down
 *    9       Quintuple Tap Up
 *    10      Quintuple Tap Down
 *
 *  Copyright (c) 2018 Samuel Kadolph
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

metadata {
  definition (namespace: "smart-home", name: "HomeSeer WD200+ Dimmer", author: "samuelkadolph") {
    capability "Actuator"
    capability "Button"
    capability "Configuration"
    capability "Health Check"
    capability "Holdable Button"
    capability "Indicator"
    capability "Polling"
    capability "Switch Level"
    capability "Switch"

    command "holdDown1"
    command "holdUp1"
    command "tapDown1"
    command "tapDown2"
    command "tapDown3"
    command "tapDown4"
    command "tapDown5"
    command "tapUp1"
    command "tapUp2"
    command "tapUp3"
    command "tapUp4"
    command "tapUp5"

    fingerprint mfr: "000C", model: "3036", deviceJoinName: "HomeSeer WD200+ Dimmer"

    tiles(scale: 2) {
      multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
        tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
          attributeState "off", label: "OFF", action: "switch.on", icon: "st.Home.home30", backgroundColor: "#FFFFFF", nextState: "turningOn"
          attributeState "on", label: "ON", action: "switch.off", icon: "st.Home.home30", backgroundColor: "#00A0DC", nextState: "turningOff"
          attributeState "turningOn", label: "TURNINGON", action: "switch.off", icon: "st.Home.home30", backgroundColor:"#00A0DC", nextState: "turningOff"
          attributeState "turningOff", label: "TURNINGOFF", action: "switch.on", icon: "st.Home.home30", backgroundColor:"#FFFFFF", nextState: "turningOn"
        }
        tileAttribute ("device.level", key: "SLIDER_CONTROL") {
          attributeState "level", action: "switch level.setLevel"
        }
      }

      valueTile("tapUp1", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap   ▲  ", action: "tapUp1", backgroundColor: "#FFFFFF"
      }
      valueTile("holdUp", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Hold   ▲  ", action: "holdUp", backgroundColor: "#FFFFFF"
      }
      valueTile("tapUp2", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap  ▲▲ ", action: "tapUp2", backgroundColor: "#FFFFFF"
      }
      valueTile("tapUp3", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▲▲▲", action: "tapUp3", backgroundColor: "#FFFFFF"
      }
      valueTile("tapUp4", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▲▲▲▲", action: "tapUp4", backgroundColor: "#FFFFFF"
      }
      valueTile("tapUp5", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▲▲▲▲▲", action: "tapUp5", backgroundColor: "#FFFFFF"
      }

      valueTile("tapDown1", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap   ▼  ", action: "tapDown1", backgroundColor: "#FFFFFF"
      }
      valueTile("holdDown", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Hold   ▼  ", action: "holdDown", backgroundColor: "#FFFFFF"
      }
      valueTile("tapDown2", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap  ▼▼ ", action: "tapDown2", backgroundColor: "#FFFFFF"
      }
      valueTile("tapDown3", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▼▼▼", action: "tapDown3", backgroundColor: "#FFFFFF"
      }
      valueTile("tapDown4", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▼▼▼▼", action: "tapDown4", backgroundColor: "#FFFFFF"
      }
      valueTile("tapDown5", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▼▼▼▼▼", action: "tapDown5", backgroundColor: "#FFFFFF"
      }

      standardTile("refresh", "device.refresh", width: 2, height: 2, decoration: "flat") {
        state "default", label: "", action: "refresh.refresh", icon: "st.secondary.refresh"
      }

      main "switch"
      details "switch", "tapUp1", "holdUp", "tapUp2", "tapUp3", "tapUp4", "tapUp5", "tapDown1", "holdDown", "tapDown2", "tapDown3", "tapDown4", "tapDown5", "refresh"
    }

    preferences {
      input "reverseSwitch", "bool", title: "Reverse the paddles if the switch was installed upside down.", defaultValue: false, displayDuringSetup: true
      input "doubleTapUpForFullBrightness", "bool", title: "Double tap the up paddle for full brightness.", defaultValue: true, displayDuringSetup: true
      input "doubleTapDownForPreviousBrightness", "bool", title: "Double tap the down paddle to go back to the previous brightness", defaultValue: true, displayDuringSetup: true

      input "localRampRate", "number", title: "Ramp rate for local control in seconds.\r\nRange: 0-90\r\nDefault: 3", required: false, defaultValue: 3, range: "0..90"
      input "remoteRampRate", "number", title: "Ramp rate for remote control in seconds.\r\nRange: 0-90\r\nDefault: 3", required: false, defaultValue: 3, range: "0..90"

      input "shades", "capability.windowShade", title: "Shades to control with triple tap.", multiple: true
    }
  }
}

def configure() {
  log.debug("configure()")

  doConfigure()
}

def off() {
  log.debug("off()")

  sendSetCommand(0)
}

def on() {
  log.debug "on()"

  sendSetCommand(0xFF, device.latestValue("level"))
}

def poll() {
  zwave.switchMultilevelV1.switchMultilevelGet().format()
}

def setLevel(Number value) {
  log.debug "setLevel(${value})"

  sendSetCommand(value)
}

def holdDown1() {
  log.debug "holdDown1()"

  sendButtonEvent(1, "down", "held")
}

def holdUp1() {
  log.debug "holdUp1()"

  sendButtonEvent(1, "up", "held")
}

def tapDown1() {
  log.debug "tapDown1()"

  sendButtonEvent(1, "down")
}

def tapDown2() {
  log.debug "tapDown2()"

  sendButtonEvent(2, "down")
}

def tapDown3() {
  log.debug "tapDown3()"

  sendButtonEvent(3, "down")
}

def tapDown4() {
  log.debug "tapDown4()"

  sendButtonEvent(4, "down")
}

def tapDown5() {
  log.debug "tapDown5()"

  sendButtonEvent(5, "down")
}

def tapUp1() {
  log.debug "tapUp1()"

  sendButtonEvent(1, "up")
  on()
}

def tapUp2() {
  log.debug "tapUp2()"

  sendButtonEvent(2, "up")
}

def tapUp3() {
  log.debug "tapUp3()"

  sendButtonEvent(3, "up")
}

def tapUp4() {
  log.debug "tapUp4()"

  sendButtonEvent(4, "up")
}

def tapUp5() {
  log.debug "tapUp5()"

  sendButtonEvent(5, "up")
}

def parse(String description) {
  def cmd = zwave.parse(description)

  if (cmd) {
    return zwaveEvent(cmd)
  } else {
    return null
  }
}

def ping() {
  log.debug("ping()")

  doRefresh()
}

def updated() {
  log.debug("updated()")

  doConfigure()
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
  log.debug cmd

  handleReport(cmd)
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneNotification cmd) {
  log.debug cmd

  def tapCount = null
  def paddle = null
  def value = "pushed"

  switch (cmd.sceneNumber) {
    case 1:
      paddle = "up"
      break
    case 2:
      paddle = "down"
      break
    default:
      log.error("CentralSceneNotification unknown sceneNumber '$cmd.sceneNumber'")
      return
  }

  switch (cmd.keyAttributes) {
    case 0:
      tapCount = 1
      break
    case 1:
      // paddle released
      value = "held"
      break
    case 2:
      // paddle held
      // tapCount = 1
      value = "held"
      break
    case 3:
    case 4:
    case 5:
    case 6:
      tapCount = cmd.keyAttributes - 1
      break
    default:
      log.error("CentralSceneNotification unknown keyAttributes '$cmd.keyAttributes'")
      return
  }

  sendButtonEvent(tapCount, paddle, value)

  if (doubleTapUpForFullBrightness && tapCount == 2 && paddle == "up") {
    // state.previousBrightness = device.level
    // doTapUp2()
  }

  if (doubleTapDownForPreviousBrightness && tapCount == 2 && paddle == "down") {
  }
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelReport cmd) {
  log.debug cmd

  handleReport(cmd)
}

private buildRampRateConfiguration(Number parameter, Number value) {
  if (value != null) {
    zwave.configurationV2.configurationSet(configurationValue: [Math.max(Math.min(value, 90), 0)], parameterNumber: 11, size: 1).format()
  }
}

private doConfigure() {
  sendEvent(name: "checkInterval", value: 960, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID, offlinePingable: "1"])
  sendEvent(name: "numberOfButtons", value: 12)

  def cmds = []

  // cmds += buildRampRateConfiguration(11, remoteRampRate)
  // cmds += buildRampRateConfiguration(12, localRampRate)

  cmds
}

private doRefresh() {
  def cmds = []

  cmds << zwave.switchMultilevelV1.switchMultilevelGet().format()

  response(cmds)
}
private handleReport(physicalgraph.zwave.Command cmd) {
  def cmds = []

  def a = cmd.value == 0 ? "off" : "on"
  log.debug("cmd.value=${a}")
  // cmds << createEvent(name: "switch", value: cmd.value == 0 ? "off" : "on")
  sendEvent(name: "switch", value: cmd.value == 0 ? "off" : "on", displayed: true)
  sendEvent(name: "level", value: cmd.value, unit: "%")

  // if (state.changeLevel) {
  //   log.debug "changeLevel is true"
  //   if (cmd.value == state.targetLevel) {
  //     log.debug "reached targetLevel of ${state.targetLevel}"

  //     state.changeLevel = false
  //   } else {
  //     log.debug "${cmd.value} != ${state.targetLevel}, getting another report"

  //     cmds << "delay 250"
  //     cmds << zwave.switchMultilevelV1.switchMultilevelGet().format()
  //   }
  // }

  cmds
}

private sendButtonEvent(Number tapCount, String paddle, String value) {
  def button = paddle == "up" ? (tapCount * 2) - 1 : tapCount * 2

  sendEvent(name: "button", value: value, data: [buttonNumber: button], descriptionText: "$device.displayName button $button was $value", isStateChange: true)
}

private sendButtonEvent(Number tapCount, String paddle) {
  sendButtonEvent(tapCount, paddle, "pushed")
}

private sendSetCommand(Number value) {
  sendSetCommand(value, value)
}

private sendSetCommand(Number value, Number target) {
  def cmds = []

  state.changeLevel = true
  state.targetLevel = target

  cmds << zwave.basicV1.basicSet(value: value).format()
  cmds << "delay 3000"
  cmds << zwave.switchMultilevelV1.switchMultilevelGet().format()

  response(cmds)
}
