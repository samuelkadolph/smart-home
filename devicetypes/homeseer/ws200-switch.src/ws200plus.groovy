/*
 *  HomeSeer HS-WS100+
 *
 *
 *
 *
 *  Buttons
 *
 */

metadata {
  definition (name: "WS200+ Switch", namespace: "HomeSeer", author: "samuelkadolph") {
    capability "Button"
    capability "Switch"

    fingerprint mfr: "000C", prod: "4447", model: "3035"
  }
}
