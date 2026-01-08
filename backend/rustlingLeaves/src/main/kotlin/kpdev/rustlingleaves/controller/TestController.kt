package kpdev.rustlingleaves.controller

import kpdev.rustlingleaves.model.control.DiceEventIdentifier
import kpdev.rustlingleaves.model.control.dice.FALL_DICE_EVENTS
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {
    @GetMapping
    fun test(): Any? {
        val prev = FALL_DICE_EVENTS
        val shorts = prev.map { DiceEventIdentifier.fromDiceEvent(it) }
        val post = shorts.map { it.toDiceEvent() }
        return prev.containsAll(post)
    }
}