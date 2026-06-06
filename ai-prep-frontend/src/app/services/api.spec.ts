
/// <reference types="jasmine" />
import { TestBed } from '@angular/core/testing';
import { ApiService } from './api';

describe('ApiService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ApiService]
    });
  });

  it('should compile properly', () => {
    expect(true).toBe(true);
  });
});