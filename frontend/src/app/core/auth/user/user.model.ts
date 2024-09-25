import { Token } from '../token/token.model';

export interface User {
  username: string;
  token: Token;
}
