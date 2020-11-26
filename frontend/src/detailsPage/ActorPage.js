import React from 'react';
import {useHistory} from "react-router-dom";
import useActor from "../hooks/useActor";

export default function ActorPage() {
  const history = useHistory();
  const [actor] = useActor();

  return (
    <>
      <div>{actor?.name}</div>
      <img alt="ActorImage" src={actor?.image}/>
      <button onClick={onCancel}>Cancel</button>
      <label>Birthday<p>{actor?.birthday}</p></label>
      <label>Place of Birth<p>{actor?.placeOfBirth}</p></label>
      <label>Biography<p>{actor?.biography}</p></label>
    </>
  )

  function onCancel() {
    history.goBack();
  }

}