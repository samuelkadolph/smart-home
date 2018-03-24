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
    capability "Button"
    capability "Switch"
    capability "Switch Level"

    fingerprint mfr: "000C", prod: "4447", model: "3036"
  }
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