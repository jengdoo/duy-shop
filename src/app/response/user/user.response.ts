import { Role } from '../../models/roles';

export interface UserResponse {
  id: number;
  fullname: string;
  address: string;
  phone_number: number;
  date_of_birth: Date;
  facebook_account_id: number;
  google_account_id: number;
  is_active: boolean;
  roles: Role;
}
