/*
 *  FortrezZ MIMOLite (Fireplace)
 *    SmartThings Device Handler that uses a FortrezZ MIMOLite to control a gas fireplace while preserving the
 *    functionality of the low voltage switch commonly installed.
 *
 *    Website: https://www.fortrezz.com/io-modules
 *
 *  Parameters
 *    See /docs/Tech+Appendix+MIMOlite+8May2017.pdf
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
  definition (namespace: "FortrezZ", name: "MIMOLite (Fireplace)", author: "samuelkadolph") {
    capability "Configuration"
    capability "Health Check"
    capability "Polling"
    capability "Refresh"
    capability "Switch"

    tiles(scale: 2) {
      multiAttributeTile(name: "switch", type: "generic", width: 6, height: 4, canChangeIcon: true) {
        tileAttribute("device.switch", key: "PRIMARY_CONTROL") {
          attributeState "off", label: "OFF", action: "switch.on", nextState: "turningOn", backgroundColor: "#FFFFFF", icon: "st.Seasonal Winter.seasonal-winter-009"
          attributeState "on", label: "ON", action: "switch.off", nextState: "turningOff", backgroundColor: "#00A0DC", icon: "st.Seasonal Winter.seasonal-winter-009"
          attributeState "turningOn", label: "TURNINGON", action: "switch.off", nextState: "turningOff", backgroundColor:"#00A0DC", icon: "st.Seasonal Winter.seasonal-winter-009"
          attributeState "turningOff", label: "TURNINGOFF", action: "switch.on", nextState: "turningOn", backgroundColor:"#FFFFFF", icon: "st.Seasonal Winter.seasonal-winter-009"
        }
      }

      standardTile("refresh", "device.switch", width: 2, height: 2, decoration: "flat") {
        state "default", label: "", action: "refresh.refresh", icon: "st.secondary.refresh"
      }

      main "switch"
      details "switch", "refresh"
    }

    preferences {
      input "timeCutoff", "number", title: "How many hours should the fireplace be on for?\r\nDefault: 4 hours"
    }
  }
}

def configure() {
  log.debug "configure()"

  _configure()
}

def off() {
  log.debug "off()"

  def cmds = []

  cmds << zwave.basicV1.basicSet(value: 0x00).format()
  cmds << zwave.switchBinaryV1.switchBinaryGet().format()

  response(delayBetween(cmds))
}

def on() {
  log.debug "on()"

  def cmds = []

  cmds << zwave.basicV1.basicSet(value: 0xFF).format()
  cmds << zwave.switchBinaryV1.switchBinaryGet().format()

  response(delayBetween(cmds))
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
  log.debug "ping()"

  _refresh()
}

def poll() {
  log.debug "poll()"

  _refresh()
}

def refresh() {
  log.debug "refresh()"

  _refresh()
}

def updated() {
  log.debug "updated()"

  _configure()
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicSet cmd) {
  def cmds = []

  if (cmd.value != state.lastSwitchState) {
    cmds << zwave.basicV1.basicSet(value: device.currentValue("switch") == "off" ? 0xFF : 0x00).format()
    cmds << zwave.switchBinaryV1.switchBinaryGet().format()
  }

  state.lastSwitchState = cmd.value

  [response(delayBetween(cmds))]
}

def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
  createEvent(name: "switch", value: cmd.value == 0x00 ? "off" : "on")
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
  [:]
}

private def _configure() {
  sendEvent(name: "checkInterval", value: 960, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID, offlinePingable: "1"])

  def cmds = []

  cmds << zwave.configurationV2.configurationSet(configurationValue: [0], parameterNumber: 3, size: 1).format()
  cmds << zwave.configurationV2.configurationSet(configurationValue: [3], parameterNumber: 8, size: 1).format()
  cmds << zwave.configurationV2.configurationSet(configurationValue: [0], parameterNumber: 11, size: 1).format()

  response(delayBetween(cmds, 500))
}

private def _refresh() {
  def cmds = []

  cmds << zwave.switchBinaryV1.switchBinaryGet().format()

  response(cmds)
}
