import React, { Component } from 'react';
import rest from '../common/rest'
import companyDtoObject from '../common/dtos/companyDtoObject'
import getDefaults from '../common/dtos/getDefaults'
import getComponents from '../common/forms/getComponents'
import StyledInput from '../common/forms/StyledInput';
import execute from '../common/execute';

const companyInit = getDefaults(companyDtoObject)

class RegisterCompany extends Component {

	constructor(props) {

		// console.log(companyInit)
		super(props);
		this.state = {
			fields: {
				...companyInit,
			},
			isLoading: false,
			isError: false,
			isSuccess: false
		}
	}

	onChangeHandler = (e) => {
		const prop = e.target.name
		const val = e.target.value

		console.log('change', prop, val)

		// not {prop: val} but {[prop]: val}

		this.setState({ fields: { ...this.state.fields, [prop]: val } })
	}

	handleRegisterCompany = () => {
		// fetch('http://localhost:8080/jobster/users/companies', {
		//     method: 'post',
		//     mode: 'no-cors',
		//     headers: { 'Content-Type': 'application/json' },
		//     body: {
		//         "companyName": "tester",
		//         "companyId": 1014090891254,
		//         "mobilePhone": "+38169010015",
		//         "email": "saqo0s0s@gmail.com",
		//         "city": "Novi Sad",
		//         "country": "Serbia",
		//         "accessRole": "ROLE_USER",
		//         "iso2Code": "SER",
		//         "countryRegion": "Vojvodina",
		//         "longitude": 38.0,
		//         "latitude": 39.1,
		//         "detailsLink": "Aaaaaaa",
		//         "username": "Test123456789012",
		//         "password": "Test12345678901",
		//         "confirmedPassword": "Test12345678901"
		//     }
		// }).then(response => console.log(response));

		
		execute(rest.companies.actions.addNewCompany, this.state.fields)
	}

	render() {
		return (
			<div>
				<div className='jumbotron'>
					<div className="container register-form">
						<div className="form">
							<div className="note">
								<p>Register Company</p>
							</div>
							<div className="form-content">
								<div className="row">
									<div className="col-md-12">
										<div className="form-group">
											<StyledInput shape={companyDtoObject.companyName} parent={this} />
										</div>
										<div className="form-group">
											<StyledInput shape={companyDtoObject.companyRegistrationNumber} parent={this} />
										</div>
										<div className="form-group">
											<StyledInput shape={companyDtoObject.mobilePhone} parent={this} />
										</div>
										<div className="form-group">
											<StyledInput shape={companyDtoObject.country} parent={this} />
										</div>

										<div className="form-group">
											<StyledInput shape={companyDtoObject.email} parent={this} />
										</div>
										<div className="form-group">
											<StyledInput shape={companyDtoObject.username} parent={this} />
										</div>
										<div className="form-group">
											<StyledInput shape={companyDtoObject.password} parent={this} />
											{/* {getComponents(companyDtoObject.password, this)} */}
										</div>
										<div className="form-group">
											<StyledInput shape={companyDtoObject.confirmedPassword} parent={this} />
										</div>

										<button type="button" onClick={this.handleRegisterCompany} className="btn btn-danger btn-lg">Login</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div> </div>
		);
	}
}
export default RegisterCompany;