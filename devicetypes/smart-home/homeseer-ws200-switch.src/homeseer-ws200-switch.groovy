/*
 *  HomeSeer HS-WS200+
 *    SmartThings Device Handler for the HomeSeer WS200+ wall switch with RGB indictator.
 *
 *    Website: https://shop.homeseer.com/products/hsws200
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
  definition (name: "HomeSeer WS200+ Switch", author: "samuelkadolph") {
    capability "Actuator"
    capability "Button"
    capability "Configuration"
    capability "Health Check"
    capability "Holdable Button"
    capability "Indicator"
    capability "Polling"
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

    fingerprint mfr: "000C", model: "3035", deviceJoinName: "HomeSeer HS-WS200+ Switch"

    tiles(scale: 2) {
      multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
        tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
          attributeState "off", label: "OFF", action: "switch.on", nextState: "turningOn", icon: "st.Home.home30", backgroundColor: "#FFFFFF"
          attributeState "on", label: "ON", action: "switch.off", nextState: "turningOff", icon: "st.Home.home30", backgroundColor: "#00A0DC"
          attributeState "turningOn", label: "TURNINGON", action: "switch.off", nextState: "turningOff", icon: "st.Home.home30", backgroundColor: "#00A0DC"
          attributeState "turningOff", label: "TURNINGOFF", action: "switch.on", nextState: "turningOn", icon: "st.Home.home30", backgroundColor: "#FFFFFF"
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
  }
}


def configure() {
  log.debug("configure()")

  _configure()
}

def holdDown1() {
  log.debug("holdDown1()")

  sendButtonEvent(1, "down", "held")
}

def holdUp1() {
  log.debug("holdUp1()")

  sendButtonEvent(1, "up", "held")
}

def off() {
  log.debug("off()")

  sendSetCommand(0x00)
}

def on() {
  log.debug("on()")

  sendSetCommand(0xFF)
}

def poll() {
  log.debug("poll()")

  sendGetCommand()
}

def tapDown1() {
  log.debug("tapDown1()")

  sendButtonEvent(1, "down")
  sendSetCommand(0x00)
}

def tapDown2() {
  log.debug("tapDown2()")

  sendButtonEvent(2, "down")
}

def tapDown3() {
  log.debug("tapDown3()")

  sendButtonEvent(3, "down")
}

def tapDown4() {
  log.debug("tapDown4()")

  sendButtonEvent(4, "down")
}

def tapDown5() {
  log.debug("tapDown5()")

  sendButtonEvent(5, "down")
}

def tapUp1() {
  log.debug("tapUp1()")

  sendButtonEvent(1, "up")
  sendSetCommand(0xFF)
}

def tapUp2() {
  log.debug("tapUp2()")

  sendButtonEvent(2, "up")
}

def tapUp3() {
  log.debug("tapUp3()")

  sendButtonEvent(3, "up")
}

def tapUp4() {
  log.debug("tapUp4()")

  sendButtonEvent(4, "up")
}

def tapUp5() {
  log.debug("tapUp5()")

  sendButtonEvent(5, "up")
}

def parse(String description) {
  def cmd = zwave.parse(description)

  if (cmd) {
    log.debug(cmd)
    return zwaveEvent(cmd)
  } else {
    return null
  }
}

def ping() {
  log.debug("ping()")

  sendGetCommand()
}

def refresh() {
  log.debug("refresh()")

  sendGetCommand()
}

def updated() {
  log.debug("updated()")

  _configure()
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
  log.debug("zwaveEvent ${cmd}")
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneNotification cmd) {
  log.debug("zwaveEvent ${cmd}")

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
      log.error("CentralSceneNotification unknown sceneNumber: '$cmd.sceneNumber'")
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
      log.error("CentralSceneNotification unknown keyAttributes: '$cmd.keyAttributes'")
      return
  }

  sendButtonEvent(tapCount, paddle, value)
}

def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
  log.debug("zwaveEvent ${cmd}")

  def newValue = cmd.value == 0x00 ? "off" : "on"

  createEvent(name: "switch", value: newValue, isStateChange: device.value == newValue)
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
  [:]
}

private sendButtonEvent(Number tapCount, String paddle) {
  sendButtonEvent(tapCount, paddle, "pushed")
}

private sendButtonEvent(Number tapCount, String paddle, String value) {
  def button = paddle == "up" ? (tapCount * 2) - 1 : tapCount * 2

  sendEvent(name: "button", value: value, data: [buttonNumber: button], descriptionText: "$device.displayName button $button was $value", isStateChange: true)
}

private sendGetCommand() {
  def cmds = []

  cmds << zwave.switchBinaryV1.switchBinaryGet().format()

  response(cmds)
}

private sendSetCommand(Number value) {
  def cmds = []

  cmds << zwave.basicV1.basicSet(value: value).format()
  cmds << "delay 250"
  cmds << zwave.switchBinaryV1.switchBinaryGet().format()

  response(cmds)
}

private _configure() {
  sendEvent(name: "checkInterval", value: 960, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID, offlinePingable: "1"])

  [:]
}
