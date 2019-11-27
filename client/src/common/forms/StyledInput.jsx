import React from 'react'

const StyledInput = (props) => {
  const { name, type, regexp, required, options, min, max, placeholder } = props.shape

//  console.log('shape', props.shape)

  return (
    <input
      type={type}
      name={name}
      regexp={regexp}
      required={required}
      min={min}
      max={max}
      className="form-control"
      placeholder={placeholder}
      value={props.parent.state.fields[name]}
      onChange={props.parent.onChangeHandler}
    />
  )
}

export default StyledInput