import "./scoring-tags.css"
import { useEffect } from "react"


export default function ScoringTags({ tags }) {

    useEffect(() => console.log(tags), [tags])

    return (
        <div className="horizontal-container center">
            <table><tbody>
                {tags?.sort((a,b) => a.fieldTypeDto.enumName.localeCompare(b.fieldTypeDto.enumName)).map((x, i) => <tr>
                    <td className="scoring-tag-img" key={i} style={{backgroundImage: `url("${process.env.PUBLIC_URL}/field-types/${x.fieldTypeDto.enumName.toLocaleLowerCase()}.jpg")`}} />
                    <td>{x.value} / {x.max} </td>
                </tr>)}
            </tbody></table>
        </div>
    )
}