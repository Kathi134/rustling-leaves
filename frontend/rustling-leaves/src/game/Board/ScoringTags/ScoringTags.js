import { sortSpringTags } from "../../../shared/model/scoringTagUtils"
import CrossedCirclesComponent from "./CrossedCirclesComponent"
import "./scoring-tags.css"


export default function ScoringTags({ tags }) {

    return (
        <div className="horizontal-container center">
            <table><tbody>
                {tags?.sort(sortSpringTags).map((x, i) => <tr>
                    <td className="scoring-tag-img" key={i} style={{backgroundImage: `url("${process.env.PUBLIC_URL}/field-types/${x.fieldTypeDto.enumName.toLocaleLowerCase()}.jpg")`}} />
                    <td><CrossedCirclesComponent val={x.value} max={x.max} /></td>
                </tr>)}
            </tbody></table>
        </div>
    )
}