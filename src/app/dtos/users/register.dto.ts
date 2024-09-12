import { IsDate, IsNotEmpty, IsPhoneNumber, IsString } from 'class-validator';

export class RegisterDTO {
  @IsString()
  fullname: string;
  @IsPhoneNumber()
  phone_number: string;
  address: string;
  @IsString()
  @IsNotEmpty()
  password: string;
  @IsString()
  @IsNotEmpty()
  confirm_password: string;
  @IsDate()
  date_of_birth: Date;
  facebook_account_id: number = 0;
  google_account_id: number = 0;
  roles_id: number = 1;
  constructor(data: any) {
    this.fullname = data.fullname;
    this.phone_number = data.phone_number;
    this.password = data.password;
    this.address = data.address;
    this.confirm_password = data.confirm_password;
    this.date_of_birth = data.date_of_birth;
    this.facebook_account_id = data.facebook_account_id;
    this.google_account_id = data.google_account_id;
    this.roles_id = data.roles_id;
  }
}
