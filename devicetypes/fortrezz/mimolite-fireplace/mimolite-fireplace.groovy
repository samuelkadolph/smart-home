/*
 *  FortrezZ MIMOLite
 *
 *  https://www.fortrezz.com/io-modules
 */

metadata {
  definition (namespace: "FortrezZ", name: "MIMOLite Fireplace", author: "samuelkadolph") {
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
