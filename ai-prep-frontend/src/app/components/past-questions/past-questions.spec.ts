import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PastQuestionsComponent } from './past-questions'; // Sahi class map ki

describe('PastQuestionsComponent', () => {
  let component: PastQuestionsComponent;
  let fixture: ComponentFixture<PastQuestionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PastQuestionsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PastQuestionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});