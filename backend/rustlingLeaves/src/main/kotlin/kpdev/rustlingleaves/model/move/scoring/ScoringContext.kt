package kpdev.rustlingleaves.model.move.scoring

import kpdev.rustlingleaves.model.skeleton.FieldType
import kpdev.rustlingleaves.model.skeleton.PlayerCard
import kpdev.rustlingleaves.model.skeleton.ScoringTag
import kpdev.rustlingleaves.model.skeleton.get

class ScoringContext(
    defaultFieldType: FieldType,
    playerCard: PlayerCard,
    val otherPlayerCards: Set<PlayerCard>?,
    val scoringTag: ScoringTag = playerCard.scoringTags[defaultFieldType],
    val allScoringTags: Set<ScoringTag> = playerCard.scoringTags,
)