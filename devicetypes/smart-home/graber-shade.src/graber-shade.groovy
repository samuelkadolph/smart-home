/*
 *  Graber Shade
 *
 *  Copyright (c) 2019 Samuel Kadolph
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
  definition (namespace: "smart-home", name: "Graber Shade", author: "samuelkadolph") {
    capability "Actuator"
    capability "Battery"
    capability "Button"
    capability "Configuration"
    // capability "Health Check"
    // capability "Polling"
    capability "Refresh"
    capability "Switch Level"
    capability "Switch"
    capability "Window Shade"

    fingerprint mfr: "026E", model: "5A31", deviceJoinName: "Graber Shade"

    tiles(scale: 2) {
      multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
        tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
          attributeState "off", label: "CLOSED", action: "switch.on", nextState: "turningOn", icon: "st.Home.home9", backgroundColor: "#FFFFFF"
          attributeState "on", label: "OPEN", action: "switch.off", nextState: "turningOff", icon: "st.Home.home9", backgroundColor: "#00A0DC"
          attributeState "turningOn", label: "OPENING", action: "switch.off", nextState: "turningOff", icon: "st.Home.home9", backgroundColor: "#00A0DC"
          attributeState "turningOff", label: "CLOSING", action: "switch.on", nextState: "turningOn", icon: "st.Home.home9", backgroundColor: "#FFFFFF"
        }
        tileAttribute ("device.level", key: "SLIDER_CONTROL") {
          attributeState "level", action: "switch level.setLevel"
        }
      }

      standardTile("refresh", "device.refresh", width: 2, height: 2, decoration: "flat") {
        state "default", label: "", action: "refresh.refresh", icon: "st.secondary.refresh"
      }

      main "switch"
      details "switch", "refresh"
    }

    preferences {
    }
  }
}

def close() {
  log.debug("close()")

  sendSetCommand(0)
}

def off() {
  log.debug("off()")

  sendSetCommand(0)
}

def on() {
  log.debug "on()"

  sendSetCommand(0xFF, device.latestValue("level"))
}

def open() {
  sendSetCommand(0xFF)
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

  sendRefresh()
}

def setLevel(Number value) {
  log.debug("setLevel(${value})")

  sendSetCommand(value)
}

def refresh() {
  log.debug("refresh()")

  sendRefresh()
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelReport cmd) {
  def a = cmd.value == 0 ? "off" : "on"
  log.debug("cmd.value=${a}")
  sendEvent(name: "switch", value: cmd.value == 0 ? "off" : "on", displayed: true)
  sendEvent(name: "level", value: cmd.value, unit: "%")
}

private sendRefresh() {
  def cmds = []

  cmds << zwave.switchMultilevelV1.switchMultilevelGet().format()
  cmds << zwave.batteryV1.batteryGet().format()

  delayBetween(cmds, 100)
}

private sendSetCommand(Number value) {
  def cmds = []

  cmds << zwave.basicV1.basicSet(value: value).format()
  cmds << "delay 5000"
  cmds << zwave.switchMultilevelV1.switchMultilevelGet().format()

  response(cmds)
}
