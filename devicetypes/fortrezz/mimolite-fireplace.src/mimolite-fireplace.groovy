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
    capability "Polling"
    capability "Switch"

    tiles(scale: 2) {
      standardTile("switch", "device.switch", width: 6, height: 4, canChangeIcon: true) {
        state "off", label: "OFF", action: "switch.on", nextState: "turningOn", backgroundColor: "#FFFFFF", icon: "st.Seasonal Winter.seasonal-winter-009"
        state "on", label: "ON", action: "switch.off", nextState: "turningOff", backgroundColor: "#00A0DC", icon: "st.Seasonal Winter.seasonal-winter-009"
        state "turningOn", label: "TURNINGON", action: "switch.off", nextState: "turningOff", backgroundColor:"#00A0DC", icon: "st.Seasonal Winter.seasonal-winter-009"
        state "turningOff", label: "TURNINGOFF", action: "switch.on", nextState: "turningOn", backgroundColor:"#FFFFFF", icon: "st.Seasonal Winter.seasonal-winter-009"
      }

      main "switch"
      details "switch"
    }

    preferences {
    }
  }
}

def configure() {
  log.debug "configure()"
}

def off() {
  log.debug "off()"

  def cmds = []

  cmds << zwave.basicV1.basicSet(value: 0x00).format()
  cmds << "delay 250"
  cmds << zwave.switchMultilevelV1.switcBinaryGet().format()

  response(cmds)
}

def on() {
  log.debug "on()"

  def cmds = []

  cmds << zwave.basicV1.basicSet(value: 0xFF).format()
  cmds << "delay 250"
  cmds << zwave.switchMultilevelV1.switcBinaryGet().format()

  response(cmds)
}

def poll() {
  def cmds = []

  cmds << zwave.switchMultilevelV1.switcBinaryGet().format()

  // TODO analog vs digital sensor read
  // cmds <<

  response(cmds)
}

def parse(String description) {
  def cmd = zwave.parse(description)

  if (cmd) {
    return zwaveEvent(cmd)
  } else {
    return null
  }
}

def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
  log.debug cmd

  createEvent(name: "switch", value: cmd.value == 0x00 ? "off" : "on")
}
