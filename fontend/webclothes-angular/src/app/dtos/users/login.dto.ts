import { IsDate, IsNotEmpty, IsPhoneNumber, IsString } from 'class-validator';

export class LoginDTO {
  @IsPhoneNumber()
  phone_number: string;
  @IsString()
  @IsNotEmpty()
  password: string;
  // roles_id: number;
  constructor(data: any) {
    this.phone_number = data.phone_number;
    this.password = data.password;
    // this.roles_id = data.role_id;
  }
}
