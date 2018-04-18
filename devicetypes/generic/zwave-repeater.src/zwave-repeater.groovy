/*
 * Z-Wave Repeater
 *
 * A
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
  definition (namespace: "generic", name: "Z-Wave Repeater", author: "samuelkadolph") {
    capability "Health Check"
    capability "Refresh"

    fingerprint mfr: "0086", prod: "0104", model: "0075", deviceJoinName: "Z-Wave Repeater" // Aeotec Range Extender 6

    tiles(scale: 2) {
      multiAttributeTile(name: "status", type: "generic", width: 6, height: 4) {
        tileAttribute("device.status", key: "PRIMARY_CONTROL") {
          attributeState "unknown", label: "UNKNOWN", backgroundColor:"#FFFFFF", icon: "https://cdn.rawgit.com/samuelkadolph/smart-home/d3e6730/icons/zwave-repeater.png"
          attributeState "online", label: "ONLINE", backgroundColor: "#00A0DC", icon: "https://cdn.rawgit.com/samuelkadolph/smart-home/d3e6730/icons/zwave-repeater.png"
          attributeState "offline", label: "OFFLINE", backgroundColor: "#FFFFFF", icon: "https://cdn.rawgit.com/samuelkadolph/smart-home/d3e6730/icons/zwave-repeater.png"
        }
      }

      standardTile("refresh", "device.refresh", width: 2, height: 2, decoration: "flat") {
        state "default", label: "", action: "refresh.refresh", icon: "st.secondary.refresh"
      }

      main "status"
      details "status", "refresh"
    }
  }
}

def parse(String description) {
  def cmd = zwave.parse(description)

  if (cmd) {
    log.debug cmd

    return zwaveEvent(cmd)
  } else {
    return null
  }
}

def ping() {
  log.debug "ping()"

  _refresh()
}

def refresh() {
  log.debug "refresh()"

  _refresh()
}

def updated() {
  log.debug "updated()"

  def cmds = []

  sendEvent(name: "checkInterval", value: 1920, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID, offlinePingable: "1"])

  cmds << zwave.manufacturerSpecificV2.manufacturerSpecificGet().format()
  cmds << zwave.versionV1.versionGet().format()

  response(cmds)
}

def zwaveEvent(physicalgraph.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
}

def zwaveEvent(physicalgraph.zwave.commands.versionv1.VersionReport cmd) {
  def firmware = "${cmd.applicationVersion}.${cmd.applicationSubVersion.toString().padLeft(2, "0")}"

  updateDataValue("firmware", firmware)
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
  [:]
}

private def _refresh() {
  def cmds = []

  cmds << zwave.versionV1.versionGet().format()

  response(cmds)
}
