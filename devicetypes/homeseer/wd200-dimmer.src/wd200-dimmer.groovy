/*
 *  HomeSeer HS-WD100+
 *
 *
 *
 *
 *  Buttons
 *
 */

metadata {
  definition (name: "WD200+ Dimmer", namespace: "HomeSeer", author: "samuelkadolph") {
    capability "Actuator"
    capability "Button"
    capability "Configuration"
    capability "Indicator"
    capability "Polling"
    capability "Sensor"
    capability "Switch"
    capability "Switch Level"

    fingerprint mfr: "000C", prod: "4447", model: "3036"

    tiles(scale: 2) {
      multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
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

      standardTile("refresh", "device.switch", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
        state "default", label:'', action:"refresh.refresh", icon:"st.secondary.configure"
      }

      main "switch"
      details "switch", "refresh"
    }
  }
}

def configure() {

}

def off() {

}

def on() {

}

def poll() {
  zwave.switchMultilevelV1.switchMultilevelGet().format()
}

def setLevel(Number value) {
}

def setLevel(Number value, Number rate) {
}

def parse(String description) {
  log.debug "parse(${description})"
  def cmd = zwave.parse(description)

  if (cmd) {
    return zwaveEvent(cmd)
  } else {
    return null
  }
}