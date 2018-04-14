/*
 * MIMOLite (Fireplace)
 *
 * A SmartThings Device Handler that uses a FortrezZ MIMOLite (see https://www.fortrezz.com/io-modules) to remotely
 * control a gas fireplace while preserving the functionality of the low voltage switch commonly installed.
 *
 * Copyright (c) 2018 Samuel Kadolph
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

metadata {
  definition (namespace: "FortrezZ", name: "MIMOLite (Fireplace)", author: "samuelkadolph") {
    capability "Configuration"
    capability "Health Check"
    capability "Polling"
    capability "Refresh"
    capability "Switch"

    tiles(scale: 2) {
      multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
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
    }
  }
}

def configure() {
  log.debug "configure()"

  def cmds = []

  cmds << zwave.configurationV2.configurationSet(configurationValue: 0, parameterNumber: 11, size: 1).format()

  log.debug cmds
}

def off() {
  log.debug "off()"

  def cmds = []

  cmds << zwave.basicV1.basicSet(value: 0x00).format()
  cmds << "delay 250"
  cmds << zwave.switchBinaryV1.switchBinaryGet().format()

  response(cmds)
}

def on() {
  log.debug "on()"

  def cmds = []

  cmds << zwave.basicV1.basicSet(value: 0xFF).format()
  cmds << "delay 250"
  cmds << zwave.switchBinaryV1.switchBinaryGet().format()

  response(cmds)
}

def ping() {
  refresh()
}

def poll() {
  refresh()
}

def parse(String description) {
  def cmd = zwave.parse(description)

  if (cmd) {
    return zwaveEvent(cmd)
  } else {
    return null
  }
}

def update() {
  def cmds = []

  cmds << zwave.switchBinaryV1.switchBinaryGet().format()

  response(cmds)
}

def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
  log.debug cmd

  createEvent(name: "switch", value: cmd.value == 0x00 ? "off" : "on")
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
  log.debug cmd

  [:]
}