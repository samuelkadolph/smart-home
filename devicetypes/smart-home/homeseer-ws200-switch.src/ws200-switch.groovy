/*
 *  HomeSeer HS-WS200+
 *    A SmartThings Device Handler for the HomeSeer WS200+ RGB wall switch.
 *
 *    See: https://shop.homeseer.com/products/hsws200
 *
 *  Buttons
 *    TODO
 *
 *  Parameters
 *    TODO
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
  definition (name: "HomeSeer WS200+ Switch", namespace: "smart-home", author: "samuelkadolph") {
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
          attributeState "off", label: "OFF", action: "switch.on", icon: "st.Home.home30", backgroundColor: "#FFFFFF", nextState: "turningOn"
          attributeState "on", label: "ON", action: "switch.off", icon: "st.Home.home30", backgroundColor: "#00A0DC", nextState: "turningOff"
          attributeState "turningOn", label: "TURNINGON", action: "switch.off", icon: "st.Home.home30", backgroundColor:"#00A0DC", nextState: "turningOff"
          attributeState "turningOff", label: "TURNINGOFF", action: "switch.on", icon: "st.Home.home30", backgroundColor:"#FFFFFF", nextState: "turningOn"
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

      standardTile("refresh", "device.switch", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
        state "default", label:"", action:"configure", icon:"st.secondary.configure"
      }

      main "switch"
      details "switch", "tapUp1", "holdUp", "tapUp2", "tapUp3", "tapUp4", "tapUp5", "tapDown1", "holdDown", "tapDown2", "tapDown3", "tapDown4", "tapDown5", "refresh"
  }
}


def configure() {
  log.debug("configure()")

  _configure()
}

def holdDown1() {
  log.debug("holdDown1()")
}

def holdUp1() {
  log.debug("holdUp1()")
}

def off() {
  log.debug("off()")

  _set(0x00)
}

def on() {
  log.debug("on()")

  _set(0xFF)
}

def poll() {
  log.debug("poll()")

  _refresh()
}

def tapDown1() {
  log.debug("tapDown1()")
}

def tapDown2() {
  log.debug("tapDown2()")
}

def tapDown3() {
  log.debug("tapDown3()")
}

def tapDown4() {
  log.debug("tapDown4()")
}

def tapDown5() {
  log.debug("tapDown5()")
}

def tapUp1() {
  log.debug("tapUp1()")
}

def tapUp2() {
  log.debug("tapUp2()")
}

def tapUp3() {
  log.debug("tapUp3()")
}

def tapUp4() {
  log.debug("tapUp4()")
}

def tapUp5() {
  log.debug("tapUp5()")
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

  _refresh()
}

def refresh() {
  log.debug("refresh()")

  _refresh()
}

def updated() {
  log.debug("updated()")

  _configure()
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneNotification cmd) {
}

def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
  createEvent(name: "switch", value: cmd.value == 0x00 ? "off" : "on")
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
  [:]
}

private _configure() {
  sendEvent(name: "checkInterval", value: 960, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID, offlinePingable: "1"])

  []
}

private _refresh() {
  def cmds = []

  cmds << zwave.switchBinaryV1.switchBinaryGet().format()

  response(cmds)
}

private _set(Number value) {
  def cmds = []

  cmds << zwave.basicV1.basicSet(value: value).format()
  cmds << "delay 250"
  cmds << zwave.switchMultilevelV1.switchBinaryGet().format()

  response(cmds)
}
